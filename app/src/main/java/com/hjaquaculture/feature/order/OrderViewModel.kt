package com.hjaquaculture.feature.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hjaquaculture.common.base.DeliveryMethod
import com.hjaquaculture.common.base.OrderStatus
import com.hjaquaculture.common.base.TradeSymbol
import com.hjaquaculture.data.local.repository.OrderRepository
import com.hjaquaculture.data.local.repository.PurchaseOrderRepository
import com.hjaquaculture.data.local.repository.SaleOrderRepository
import com.hjaquaculture.domain.model.OrderItemsData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepo: OrderRepository,
    private val saleOrderRepo: SaleOrderRepository,
    private val purOrderRepo: PurchaseOrderRepository
) : ViewModel() {

    // 过滤状态
    private val _searchQuery = MutableStateFlow("")
    private val _selectedSymbol = MutableStateFlow<TradeSymbol?>(null)
    private val _selectedDeliveryMethod = MutableStateFlow<DeliveryMethod?>(null)
    private val _selectedStatus = MutableStateFlow<OrderStatus?>(null)

    // 暴露给 UI 观察
    val searchQuery = _searchQuery.asStateFlow()
    val selectedSymbol = _selectedSymbol.asStateFlow()
    val selectedDeliveryMethod = _selectedDeliveryMethod.asStateFlow()
    val selectedStatus = _selectedStatus.asStateFlow()

    // 过滤动作
    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun onSymbolSelected(symbol: TradeSymbol?) { _selectedSymbol.value = symbol }
    fun onDeliveryMethodSelected(method: DeliveryMethod?) { _selectedDeliveryMethod.value = method }
    fun onStatusSelected(status: OrderStatus?) { _selectedStatus.value = status }

    // 私有数据类，避免多参数 combine 难以阅读
    private data class FilterParams(
        val query: String,
        val symbol: TradeSymbol?,
        val deliveryMethod: DeliveryMethod?,
        val status: OrderStatus?
    )
    // 合并过滤条件，任一变化都触发重新分页
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val orderList: Flow<PagingData<OrderVO>> = combine(
        _searchQuery,
        _selectedSymbol,
        _selectedDeliveryMethod,
        _selectedStatus
    ) { query, symbol, method, status ->
        FilterParams(query, symbol, method, status)
    }
    .debounce(300) // 搜索框防抖
    .flatMapLatest { params ->
        orderRepo.getCombinedOrders(
            query = params.query,
            symbol = params.symbol?.name,
            type = params.deliveryMethod?.code?.toString(),
            status = params.status?.code?.toString()
        ).map { pagingData ->
            pagingData.map { domain ->
                try { domain.toVO() } catch (e: Exception) { throw e }
            }
        }
    }.cachedIn(viewModelScope)




    // 1. 驱动源：当前哪个订单被展开了
    private val _expandedOrderItem = MutableStateFlow<OrderVO?>(null)
    val expandedOrderItem = _expandedOrderItem.asStateFlow()

    // 2. 状态成品：UI 只需要订阅这个唯一的 StateFlow
    @OptIn(ExperimentalCoroutinesApi::class)
    val detailState: StateFlow<OrderDetailState> = _expandedOrderItem
        .flatMapLatest {  vo ->
            if (vo == null) {
                flowOf(OrderDetailResult.Keep)
            } else {
                // 当 ID 变化，这里会自动开启新的数据库监听
                orderRepo.getOrderDetail(vo.symbol,vo.originalId)
                .map { orderItems ->
                    val voItems = when(orderItems) {
                        is OrderItemsData.Sale -> orderItems.data.map { it.toVO(TradeSymbol.SALE) }
                        is OrderItemsData.Purchase -> orderItems.data.map { it.toVO(TradeSymbol.PURCHASE) }
                    }

                    OrderDetailResult.Success(voItems) as OrderDetailResult
                }
                .onStart {
                    emit(OrderDetailResult.Loading)
                }
                .catch {
                    emit(OrderDetailResult.Error(it.message ?: "未知错误"))
                }
            }
        }// 使用 scan 来平滑过渡状态
        .scan(OrderDetailState()) { oldState, result ->
            when (result) {
                is OrderDetailResult.Reset -> OrderDetailState()
                is OrderDetailResult.Keep -> {
                    // 仅仅关闭加载标记，但保留 items，供收缩动画使用
                    oldState.copy(isLoading = false)
                }
                is OrderDetailResult.Loading -> {
                    // 核心逻辑：保留 oldState.items，只改变加载标记
                    oldState.copy(isLoading = true, error = null, isIdle = false)
                }
                is OrderDetailResult.Success -> {
                    // 数据到达：更新数据，关闭加载标记
                    oldState.copy(items = result.data, isLoading = false, error = null)
                }
                is OrderDetailResult.Error -> {
                    oldState.copy(isLoading = false, error = result.message)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // 离线 5 秒后才真正停止数据库监听
            initialValue = OrderDetailState()
        )

    // 3. 业务动作
    fun toggleOrder(orderVO: OrderVO) {
        _expandedOrderItem.value = if (_expandedOrderItem.value == orderVO) null else orderVO
    }
}


/**
 * 订单明细状态
 * Idle: 初始状态
 * Loading: 加载中
 * Success: 加载成功
 * Error: 加载失败
 */
// 建议的资源状态封装
data class OrderDetailState(
    val items: List<OrderItemVO> = emptyList(), // 实际数据
    val isLoading: Boolean = false,             // 是否正在加载（UI 显示进度条）
    val error: String? = null,                  // 错误信息
    val isIdle: Boolean = true                  // 是否初始空闲
)

// 定义内部使用的中间结果
sealed class OrderDetailResult {
    object Reset : OrderDetailResult()
    object Keep : OrderDetailResult()
    object Loading : OrderDetailResult()
    data class Success(val data: List<OrderItemVO>) : OrderDetailResult()
    data class Error(val message: String) : OrderDetailResult()
}
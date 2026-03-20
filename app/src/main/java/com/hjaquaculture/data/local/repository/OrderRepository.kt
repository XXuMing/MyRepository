package com.hjaquaculture.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hjaquaculture.common.base.TradeSymbol
import com.hjaquaculture.data.local.dao.CombinedOrderDao
import com.hjaquaculture.data.local.dao.PurchaseOrderDao
import com.hjaquaculture.data.local.dao.PurchaseOrderItemDao
import com.hjaquaculture.data.local.dao.SaleOrderDao
import com.hjaquaculture.data.local.dao.SaleOrderItemDao
import com.hjaquaculture.data.local.mapper.toDomain
import com.hjaquaculture.domain.model.CombinedOrder
import com.hjaquaculture.domain.model.OrderItemsData
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class OrderRepository @Inject constructor(
    private val combinedDao: CombinedOrderDao,
    private val soDao: SaleOrderDao,
    private val poDao: PurchaseOrderDao,
    private val purItemDao: PurchaseOrderItemDao,
    private val saleItemDao: SaleOrderItemDao
) {
    /**
     * 获取所有订单的组合视图
     */
    fun getCombinedOrders(query: String, symbol: String?, type: String?, status:String?)
    : Flow<PagingData<CombinedOrder>>
    {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { combinedDao.getPagingSource(query, symbol, type, status)}
        ).flow.map{ pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    /**
     * 获取订单详情
     * 逻辑：根据订单类型返回不同的 Flow
     * @param symbol 订单类型
     * @param orderId 订单 ID
     */
    fun getOrderDetail(symbol: TradeSymbol, orderId: Long): Flow<OrderItemsData> {
        return when (symbol) {
            TradeSymbol.SALE -> saleItemDao.getForSaleOrder(orderId).map{ list ->
                OrderItemsData.Sale(list.map { it.toDomain() })
            }
            TradeSymbol.PURCHASE -> purItemDao.getForPurchaseOrder(orderId).map{ list ->
                OrderItemsData.Purchase(list.map { it.toDomain() })
            }
        }
    }

}
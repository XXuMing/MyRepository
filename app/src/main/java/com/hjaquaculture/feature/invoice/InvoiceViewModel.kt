package com.hjaquaculture.feature.invoice

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hjaquaculture.data.local.repository.InvoiceRepository
import com.hjaquaculture.domain.model.InvoicePaymentsData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val repo: InvoiceRepository
) : ViewModel() {

    val invoicePagingData: Flow<PagingData<InvoiceVO>> = repo.getCombinedInvoices("", null, null)
        .map { pagingData ->
            pagingData.map { domain ->
                try { domain.toVO() } catch (e: Exception) {
                    Log.e("VO_ERROR", "转换失败，数据条目ID: ${domain.id}", e); throw e
                }
            }
        }.cachedIn(viewModelScope)

    // 1. 驱动源：当前哪张账单被展开了
    private val _expandedInvoiceItem = MutableStateFlow<InvoiceVO?>(null)
    val expandedInvoiceItem = _expandedInvoiceItem.asStateFlow()

    // 2. 详情状态：监听展开的账单，自动拉取流水
    @OptIn(ExperimentalCoroutinesApi::class)
    val detailState: StateFlow<InvoiceDetailState> = _expandedInvoiceItem
        .flatMapLatest { vo ->
            if (vo == null) {
                flowOf(InvoiceDetailResult.Keep)
            } else {
                repo.getInvoiceDetail(vo.symbol, vo.originalId)
                    .map { data ->
                        val items = when (data) {
                            is InvoicePaymentsData.Sale -> data.data.map { it.toVO() }
                            is InvoicePaymentsData.Purchase -> data.data.map { it.toVO() }
                        }
                        InvoiceDetailResult.Success(items) as InvoiceDetailResult
                    }
                    .onStart { emit(InvoiceDetailResult.Loading) }
                    .catch { emit(InvoiceDetailResult.Error(it.message ?: "未知错误")) }
            }
        }
        .scan(InvoiceDetailState()) { oldState, result ->
            when (result) {
                is InvoiceDetailResult.Keep -> oldState.copy(isLoading = false)
                is InvoiceDetailResult.Loading -> oldState.copy(isLoading = true, error = null, isIdle = false)
                is InvoiceDetailResult.Success -> oldState.copy(items = result.data, isLoading = false, error = null)
                is InvoiceDetailResult.Error -> oldState.copy(isLoading = false, error = result.message)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = InvoiceDetailState()
        )

    // 3. 切换展开/收起
    fun toggleInvoice(invoiceVO: InvoiceVO) {
        _expandedInvoiceItem.value =
            if (_expandedInvoiceItem.value == invoiceVO) null else invoiceVO
    }
}

data class InvoiceDetailState(
    val items: List<PaymentItemVO> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isIdle: Boolean = true
)

sealed class InvoiceDetailResult {
    object Keep : InvoiceDetailResult()
    object Loading : InvoiceDetailResult()
    data class Success(val data: List<PaymentItemVO>) : InvoiceDetailResult()
    data class Error(val message: String) : InvoiceDetailResult()
}
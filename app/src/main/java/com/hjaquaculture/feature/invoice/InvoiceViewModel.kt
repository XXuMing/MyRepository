package com.hjaquaculture.feature.invoice

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hjaquaculture.data.local.repository.InvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    repo: InvoiceRepository
) : ViewModel() {

    // 使用 cachedIn 确保在配置更改（如旋屏）时数据不丢失，且避免重复转换
    val invoicePagingData: Flow<PagingData<InvoiceVO>> = repo.getCombinedInvoices("",null,null)
    .map { pagingData ->
        pagingData.map { domain ->
            try {
                domain.toVO()
            } catch (e: Exception) {
                Log.e("VO_ERROR", "转换失败，数据条目ID: ${domain.id}", e)
                throw e
            }
        }
    }.cachedIn(viewModelScope)

}

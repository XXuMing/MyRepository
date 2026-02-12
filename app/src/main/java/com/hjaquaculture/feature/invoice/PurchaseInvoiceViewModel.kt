package com.hjaquaculture.feature.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.hjaquaculture.domain.repository.PurchaseInvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PurchaseInvoiceViewModel @Inject constructor(
    private val repository: PurchaseInvoiceRepository
) : ViewModel() {

    val purchaseInvoicesList = repository.getAllForPaged().map{pagingData ->
        pagingData.map { it.toVO() }
    }.cachedIn(viewModelScope)

}
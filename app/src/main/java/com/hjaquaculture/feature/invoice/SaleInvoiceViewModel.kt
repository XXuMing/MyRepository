package com.hjaquaculture.feature.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.hjaquaculture.domain.repository.SaleInvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class SaleInvoiceViewModel @Inject constructor(
    private val saleInvoiceRepository: SaleInvoiceRepository
) : ViewModel() {

    val saleInvoicesList = saleInvoiceRepository.getAllForPaged().map{pagingData ->
        pagingData.map { it.toVO() }
    }.cachedIn(viewModelScope)

}
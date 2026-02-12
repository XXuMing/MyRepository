package com.hjaquaculture.domain.usecase

import com.hjaquaculture.domain.repository.SaleInvoiceRepository
import jakarta.inject.Inject

sealed class SaleInvoiceListItem{

}

class GetSaleInvoiceListUseCase @Inject constructor(
    private val siRep: SaleInvoiceRepository
){

}
package com.hjaquaculture.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hjaquaculture.common.utils.InvoiceSymbol
import com.hjaquaculture.data.local.dao.CombinedInvoiceDao
import com.hjaquaculture.data.local.dao.PurchaseInvoiceDao
import com.hjaquaculture.data.local.dao.PurchaseOrderDao
import com.hjaquaculture.data.local.dao.PurchasePaymentDao
import com.hjaquaculture.data.local.dao.SaleInvoiceDao
import com.hjaquaculture.data.local.dao.SaleOrderDao
import com.hjaquaculture.data.local.dao.SalePaymentDao
import com.hjaquaculture.data.local.mapper.toDomain
import com.hjaquaculture.domain.model.CombinedInvoice
import com.hjaquaculture.domain.model.InvoicePaymentsData
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class InvoiceRepository @Inject constructor(
    private val combinedDao: CombinedInvoiceDao,
    private val soDao: SaleOrderDao,
    private val poDao: PurchaseOrderDao,
    private val siDao: SaleInvoiceDao,
    private val piDao: PurchaseInvoiceDao,
    private val spDao: SalePaymentDao,
    private val ppDao: PurchasePaymentDao,
){

    fun getCombinedInvoices(query: String, symbol: String?, status:String?): Flow<PagingData<CombinedInvoice>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { combinedDao.pagingSource(query, symbol, status) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    fun getInvoiceDetail(symbol: InvoiceSymbol, invoiceId: Long): Flow<InvoicePaymentsData> {
        return when (symbol) {
            InvoiceSymbol.SALE -> spDao.getByInvoiceId(invoiceId).map { list ->
                InvoicePaymentsData.Sale(list.map{ it.toDomain()})
            }
            InvoiceSymbol.PURCHASE -> ppDao.getByInvoiceId(invoiceId).map{ list ->
                InvoicePaymentsData.Purchase(list.map{ it.toDomain()})
            }
        }
    }

}
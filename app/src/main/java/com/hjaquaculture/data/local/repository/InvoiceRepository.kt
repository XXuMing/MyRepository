package com.hjaquaculture.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hjaquaculture.data.local.dao.CombinedInvoiceDao
import com.hjaquaculture.data.local.dao.PurchaseInvoiceDao
import com.hjaquaculture.data.local.dao.PurchaseOrderDao
import com.hjaquaculture.data.local.dao.SaleInvoiceDao
import com.hjaquaculture.data.local.dao.SaleOrderDao
import com.hjaquaculture.data.local.entity.CombinedInvoiceView
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class InvoiceRepository @Inject constructor(
    private val combinedDao: CombinedInvoiceDao,
    private val soDao: SaleOrderDao,
    private val poDao: PurchaseOrderDao,
    private val siDao: SaleInvoiceDao,
    private val piDao: PurchaseInvoiceDao
){

    fun getCombinedInvoices(query: String, symbol: String?, status:String?): Flow<PagingData<CombinedInvoiceView>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { combinedDao.pagingSource(query, symbol, status) }
        ).flow
    }


}
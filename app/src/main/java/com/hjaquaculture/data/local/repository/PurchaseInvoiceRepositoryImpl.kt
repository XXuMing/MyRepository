package com.hjaquaculture.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.hjaquaculture.common.utils.OrderManager
import com.hjaquaculture.common.utils.OrderPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.PurchaseInvoiceDao
import com.hjaquaculture.data.local.entity.PurchaseInvoice
import com.hjaquaculture.domain.repository.PurchaseInvoiceRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class PurchaseInvoiceRepositoryImpl @Inject constructor(
    private val database: LocalDatabase,
    private val dao: PurchaseInvoiceDao,
    private val orderManager: OrderManager
) : PurchaseInvoiceRepository {

    /**
     * 添加采购账单
     */
    override suspend fun add(purchaseInvoice: PurchaseInvoice):Long{
        return database.withTransaction {
            val newId = dao.insert(purchaseInvoice)
            val sn = orderManager.generateSn(OrderPrefix.PURCHASE_INVOICE, newId)
            dao.updateSn(newId, sn)
            newId
        }
    }

    override suspend fun add(purchaseInvoices: List<PurchaseInvoice>): List<Long> {
        return dao.insertAll(purchaseInvoices)
    }

    override suspend fun delete(purchaseInvoice: PurchaseInvoice): Int {
        return dao.delete(purchaseInvoice)
    }

    override suspend fun delete(purchaseInvoiceIds: List<Long>): Int {
        return dao.deleteByIds(purchaseInvoiceIds)
    }

    override suspend fun deleteById(purchaseInvoiceId: Long): Int {
        return dao.deleteById(purchaseInvoiceId)
    }

    override suspend fun deleteByIds(purchaseInvoiceIds: List<Long>): Int {
        return dao.deleteByIds(purchaseInvoiceIds)
    }

    override suspend fun deleteBySn(sn: String): Int {
        return dao.deleteBySn(sn)
    }

    override suspend fun deleteBySns(sns: List<String>): Int {
        return dao.deleteBySns(sns)
    }

    override suspend fun deleteAll(): Int {
        return dao.deleteAll()
    }

    override suspend fun update(purchaseInvoice: PurchaseInvoice): Int {
        return dao.update(purchaseInvoice)
    }

    override suspend fun isDatabaseEmpty(): Boolean {
        return dao.getCount() == 0
    }

    override suspend fun getById(purchaseInvoiceId: Long): PurchaseInvoice {
        return dao.getById(purchaseInvoiceId)
    }

    override fun getBySnForFlow(sn: String): Flow<List<PurchaseInvoice>> {
        return dao.getAllBySnForFlow(sn)
    }

    override fun getAllForFlow(): Flow<List<PurchaseInvoice>> {
        return dao.getAllForFlow()
    }

    override fun getBySnForPaged(sn: String): Flow<PagingData<PurchaseInvoice>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllBySnForPaged(sn) }
        ).flow
    }

    override fun getAllForPaged(): Flow<PagingData<PurchaseInvoice>>  {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllForPaged() }
        ).flow
    }

    override fun getByOrderId(orderId: Long): Flow<List<PurchaseInvoice>> {
        return dao.getByOrderId(orderId)
    }

    override fun getPurchaseInvoicesBySupplierId(supplierId: Long): Flow<List<PurchaseInvoice>> {
        return dao.getBySupplierId(supplierId)
    }

    override fun getPurchaseInvoicesByUserId(userId: Long): Flow<List<PurchaseInvoice>> {
        return dao.getByUserId(userId)
    }

    override fun getPurchaseInvoicesByStatus(status: String): Flow<List<PurchaseInvoice>> {
        return dao.getByStatus(status)
    }

    override fun getPurchaseInvoicesByCreatedAt(createdAt: Long): Flow<List<PurchaseInvoice>> {
        return dao.getByCreatedAt(createdAt)
    }
}
package com.hjaquaculture.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.hjaquaculture.common.base.OrderManager
import com.hjaquaculture.common.base.SnPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.PurchaseInvoiceDao
import com.hjaquaculture.data.local.entity.PurchaseInvoiceEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class PurchaseInvoiceRepository @Inject constructor(
    private val database: LocalDatabase,
    private val dao: PurchaseInvoiceDao,
    private val orderManager: OrderManager
) {

    /**
     * 添加采购账单
     */
    suspend fun add(purchaseInvoiceEntity: PurchaseInvoiceEntity):Long{
        return database.withTransaction {
            val newId = dao.insert(purchaseInvoiceEntity)
            val sn = orderManager.generateSn(SnPrefix.PURCHASE_INVOICE, newId)
            dao.updateSn(newId, sn)
            newId
        }
    }

    suspend fun add(purchaseInvoiceEntities: List<PurchaseInvoiceEntity>): List<Long> {
        return dao.insertAll(purchaseInvoiceEntities)
    }

    suspend fun delete(purchaseInvoiceEntity: PurchaseInvoiceEntity): Int {
        return dao.delete(purchaseInvoiceEntity)
    }

    suspend fun delete(purchaseInvoiceIds: List<Long>): Int {
        return dao.deleteByIds(purchaseInvoiceIds)
    }

    suspend fun deleteById(purchaseInvoiceId: Long): Int {
        return dao.deleteById(purchaseInvoiceId)
    }

    suspend fun deleteByIds(purchaseInvoiceIds: List<Long>): Int {
        return dao.deleteByIds(purchaseInvoiceIds)
    }

    suspend fun deleteBySn(sn: String): Int {
        return dao.deleteBySn(sn)
    }

    suspend fun deleteBySns(sns: List<String>): Int {
        return dao.deleteBySns(sns)
    }

    suspend fun deleteAll(): Int {
        return dao.deleteAll()
    }

    suspend fun update(purchaseInvoiceEntity: PurchaseInvoiceEntity): Int {
        return dao.update(purchaseInvoiceEntity)
    }

    suspend fun isDatabaseEmpty(): Boolean {
        return dao.getCount() == 0
    }

    suspend fun getById(purchaseInvoiceId: Long): PurchaseInvoiceEntity {
        return dao.getById(purchaseInvoiceId)
    }

    fun getBySnForFlow(sn: String): Flow<List<PurchaseInvoiceEntity>> {
        return dao.getAllBySnForFlow(sn)
    }

    fun getAllForFlow(): Flow<List<PurchaseInvoiceEntity>> {
        return dao.getAllForFlow()
    }

    fun getBySnForPaged(sn: String): Flow<PagingData<PurchaseInvoiceEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllBySnForPaged(sn) }
        ).flow
    }

    fun getAllForPaged(): Flow<PagingData<PurchaseInvoiceEntity>>  {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllForPaged() }
        ).flow
    }


    fun getPurchaseInvoicesBySupplierId(supplierId: Long): Flow<List<PurchaseInvoiceEntity>> {
        return dao.getBySupplierId(supplierId)
    }


    fun getPurchaseInvoicesByStatus(status: String): Flow<List<PurchaseInvoiceEntity>> {
        return dao.getByStatus(status)
    }

    fun getPurchaseInvoicesByCreatedAt(createdAt: Long): Flow<List<PurchaseInvoiceEntity>> {
        return dao.getByCreatedAt(createdAt)
    }
}
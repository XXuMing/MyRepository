package com.hjaquaculture.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.hjaquaculture.common.base.OrderManager
import com.hjaquaculture.common.base.SnPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.SaleInvoiceDao
import com.hjaquaculture.data.local.entity.SaleInvoiceEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class SaleInvoiceRepository @Inject constructor(
    private val database: LocalDatabase,
    private val dao: SaleInvoiceDao,
    private val orderManager: OrderManager
) {

    /**
     * 添加销售发票
     */
     suspend fun add(saleInvoiceEntity: SaleInvoiceEntity): Long {
        return database.withTransaction {
            val newId = dao.insert(saleInvoiceEntity)
            val sn = orderManager.generateSn(SnPrefix.SALE_INVOICE, newId)
            dao.updateSn(newId, sn)
            newId
        }
    }

    suspend fun add(saleInvoiceEntities: List<SaleInvoiceEntity>): List<Long> {
        return dao.insertAll(saleInvoiceEntities)
    }

    suspend fun delete(saleInvoiceEntity: SaleInvoiceEntity): Int {
        return dao.delete(saleInvoiceEntity)
    }

    suspend fun delete(saleInvoiceIds: List<Long>): Int {
        return dao.deleteByIds(saleInvoiceIds)
    }

    suspend fun deleteById(saleInvoiceId: Long): Int {
        return dao.deleteById(saleInvoiceId)
    }

    suspend fun deleteByIds(saleInvoiceIds: List<Long>): Int {
        return dao.deleteByIds(saleInvoiceIds)
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

    suspend fun update(saleInvoiceEntity: SaleInvoiceEntity): Int {
        return dao.update(saleInvoiceEntity)
    }

    suspend fun isDatabaseEmpty(): Boolean {
        return dao.getCount() == 0
    }

    suspend fun getById(saleInvoiceId: Long): SaleInvoiceEntity {
        return dao.getById(saleInvoiceId)
    }

    fun getBySn(sn: String): Flow<List<SaleInvoiceEntity>> {
        return dao.getAllBySnForFlow(sn)
    }

    fun getAll():Flow<List<SaleInvoiceEntity>>{
        return dao.getAllForFlow()
    }

    fun getBySnForPaged(sn: String): Flow<PagingData<SaleInvoiceEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllBySnForPaged(sn) }
        ).flow
    }

    /**
     * 为 Paging 3 提供分页数据源，获取所有销售发票。
     * @return 返回 PagingSource
     */
    fun getAllForPaged(): Flow<PagingData<SaleInvoiceEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllForPaged() }
        ).flow
    }

    fun getSaleInvoicesByCustomerId(customerId: Long): Flow<List<SaleInvoiceEntity>> {
        return dao.getByCustomerId(customerId)
    }


    fun getSaleInvoicesByStatus(status: String): Flow<List<SaleInvoiceEntity>> {
        return dao.getByStatus(status)
    }

    fun getSaleInvoicesByCreatedAt(createdAt: Long): Flow<List<SaleInvoiceEntity>> {
        return dao.getByCreatedAt(createdAt)
    }
}
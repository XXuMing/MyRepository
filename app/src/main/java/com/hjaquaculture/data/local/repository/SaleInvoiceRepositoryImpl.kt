package com.hjaquaculture.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.hjaquaculture.common.utils.OrderManager
import com.hjaquaculture.common.utils.OrderPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.SaleInvoiceDao
import com.hjaquaculture.data.local.entity.SaleInvoice
import com.hjaquaculture.domain.repository.SaleInvoiceRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class SaleInvoiceRepositoryImpl @Inject constructor(
    private val database: LocalDatabase,
    private val dao: SaleInvoiceDao,
    private val orderManager: OrderManager
) : SaleInvoiceRepository {

    /**
     * 添加销售发票
     */
    override suspend fun add(saleInvoice: SaleInvoice): Long {
        return database.withTransaction {
            val newId = dao.insert(saleInvoice)
            val sn = orderManager.generateSn(OrderPrefix.SALE_INVOICE, newId)
            dao.updateSn(newId, sn)
            newId
        }
    }

    override suspend fun add(saleInvoices: List<SaleInvoice>): List<Long> {
        return dao.insertAll(saleInvoices)
    }

    override suspend fun delete(saleInvoice: SaleInvoice): Int {
        return dao.delete(saleInvoice)
    }

    override suspend fun delete(saleInvoiceIds: List<Long>): Int {
        return dao.deleteByIds(saleInvoiceIds)
    }

    override suspend fun deleteById(saleInvoiceId: Long): Int {
        return dao.deleteById(saleInvoiceId)
    }

    override suspend fun deleteByIds(saleInvoiceIds: List<Long>): Int {
        return dao.deleteByIds(saleInvoiceIds)
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

    override suspend fun update(saleInvoice: SaleInvoice): Int {
        return dao.update(saleInvoice)
    }

    override suspend fun isDatabaseEmpty(): Boolean {
        return dao.getCount() == 0
    }

    override suspend fun getById(saleInvoiceId: Long): SaleInvoice {
        return dao.getById(saleInvoiceId)
    }

    override fun getBySn(sn: String): Flow<List<SaleInvoice>> {
        return dao.getAllBySnForFlow(sn)
    }

    override fun getAll():Flow<List<SaleInvoice>>{
        return dao.getAllForFlow()
    }

    override fun getBySnForPaged(sn: String): Flow<PagingData<SaleInvoice>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllBySnForPaged(sn) }
        ).flow
    }

    /**
     * 为 Paging 3 提供分页数据源，获取所有销售发票。
     * @return 返回 PagingSource
     */
    override fun getAllForPaged(): Flow<PagingData<SaleInvoice>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllForPaged() }
        ).flow
    }

    override fun getByOrderId(orderId: Long): Flow<List<SaleInvoice>> {
        return dao.getByOrderId(orderId)
    }

    override fun getSaleInvoicesByCustomerId(customerId: Long): Flow<List<SaleInvoice>> {
        return dao.getByCustomerId(customerId)
    }

    override fun getSaleInvoicesByUserId(userId: Long): Flow<List<SaleInvoice>> {
        return dao.getByUserId(userId)
    }

    override fun getSaleInvoicesByStatus(status: String): Flow<List<SaleInvoice>> {
        return dao.getByStatus(status)
    }

    override fun getSaleInvoicesByCreatedAt(createdAt: Long): Flow<List<SaleInvoice>> {
        return dao.getByCreatedAt(createdAt)
    }
}
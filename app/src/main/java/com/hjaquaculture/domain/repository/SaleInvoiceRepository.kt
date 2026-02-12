package com.hjaquaculture.domain.repository

import androidx.paging.PagingData
import com.hjaquaculture.data.local.entity.SaleInvoice
import kotlinx.coroutines.flow.Flow

interface SaleInvoiceRepository {

    suspend fun add(saleInvoice: SaleInvoice): Long

    suspend fun add(saleInvoices: List<SaleInvoice>): List<Long>

    suspend fun delete(saleInvoice: SaleInvoice): Int

    suspend fun delete(saleInvoiceIds: List<Long>): Int

    suspend fun deleteById(saleInvoiceId: Long): Int

    suspend fun deleteByIds(saleInvoiceIds: List<Long>): Int

    suspend fun deleteBySn(sn: String): Int

    suspend fun deleteBySns(sns: List<String>): Int

    suspend fun deleteAll(): Int

    suspend fun update(saleInvoice: SaleInvoice): Int

    suspend fun isDatabaseEmpty(): Boolean

    suspend fun getById(saleInvoiceId: Long): SaleInvoice

    fun getBySn(sn: String): Flow<List<SaleInvoice>>

    fun getAll(): Flow<List<SaleInvoice>>

    fun getBySnForPaged(sn: String): Flow<PagingData<SaleInvoice>>

    fun getAllForPaged(): Flow<PagingData<SaleInvoice>>

    fun getByOrderId(orderId: Long): Flow<List<SaleInvoice>>

    fun getSaleInvoicesByCustomerId(customerId: Long): Flow<List<SaleInvoice>>

    fun getSaleInvoicesByUserId(userId: Long): Flow<List<SaleInvoice>>

    fun getSaleInvoicesByStatus(status: String): Flow<List<SaleInvoice>>

   fun getSaleInvoicesByCreatedAt(createdAt: Long): Flow<List<SaleInvoice>>

}
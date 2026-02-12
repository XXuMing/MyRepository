package com.hjaquaculture.domain.repository

import androidx.paging.PagingData
import com.hjaquaculture.data.local.entity.PurchaseInvoice
import kotlinx.coroutines.flow.Flow

interface PurchaseInvoiceRepository{

    suspend fun add(purchaseInvoice: PurchaseInvoice):Long

    suspend fun add(purchaseInvoices: List<PurchaseInvoice>): List<Long>

    suspend fun delete(purchaseInvoice: PurchaseInvoice): Int

    suspend fun delete(purchaseInvoiceIds: List<Long>): Int

    suspend fun deleteById(purchaseInvoiceId: Long): Int

    suspend fun deleteByIds(purchaseInvoiceIds: List<Long>): Int

    suspend fun deleteBySn(sn: String): Int

    suspend fun deleteBySns(sns: List<String>): Int

    suspend fun deleteAll(): Int

    suspend fun update(purchaseInvoice: PurchaseInvoice): Int

    suspend fun isDatabaseEmpty(): Boolean

    suspend fun getById(purchaseInvoiceId: Long): PurchaseInvoice

    fun getBySnForFlow(sn: String): Flow<List<PurchaseInvoice>>

    fun getAllForFlow():Flow<List<PurchaseInvoice>>

    fun getBySnForPaged(sn: String): Flow<PagingData<PurchaseInvoice>>

    fun getAllForPaged(): Flow<PagingData<PurchaseInvoice>>

    fun getByOrderId(orderId: Long): Flow<List<PurchaseInvoice>>

    fun getPurchaseInvoicesBySupplierId(supplierId: Long): Flow<List<PurchaseInvoice>>

    fun getPurchaseInvoicesByUserId(userId: Long): Flow<List<PurchaseInvoice>>

    fun getPurchaseInvoicesByStatus(status: String): Flow<List<PurchaseInvoice>>

    fun getPurchaseInvoicesByCreatedAt(createdAt: Long): Flow<List<PurchaseInvoice>>

}
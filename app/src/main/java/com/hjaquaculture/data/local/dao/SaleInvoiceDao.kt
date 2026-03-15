package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.SaleInvoiceEntity
import kotlinx.coroutines.flow.Flow

/**
 * 销售发票数据访问对象 (DAO)
 * 负责提供与销售发票相关的数据库操作方法。
 */
@Dao
interface SaleInvoiceDao {

    // --- 增加 (Create) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(invoice: SaleInvoiceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(invoices: List<SaleInvoiceEntity>): List<Long>

    // --- 删除 (Delete) ---

    @Delete
    suspend fun delete(invoice: SaleInvoiceEntity): Int

    @Query("DELETE FROM sale_invoice WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM sale_invoice WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>): Int

    @Query("DELETE FROM sale_invoice WHERE sn = :sn")
    suspend fun deleteBySn(sn: String): Int

    @Query("DELETE FROM sale_invoice WHERE sn IN (:sns)")
    suspend fun deleteBySns(sns: List<String>): Int

    @Query("DELETE FROM sale_invoice")
    suspend fun deleteAll():Int

    // --- 修改 (Update) ---

    @Update
    suspend fun update(invoice: SaleInvoiceEntity):Int

    @Query("UPDATE sale_invoice SET sn = :sn WHERE id = :id")
    suspend fun updateSn(id: Long, sn: String): Int

    // --- 查询 (Query) ---

    @Query("SELECT COUNT(*) FROM sale_invoice")
    suspend fun getCount(): Int

    @Query("SELECT * FROM sale_invoice ORDER BY created_at DESC")
    fun getAll(): Flow<List<SaleInvoiceEntity>>

    @Query("SELECT * FROM sale_invoice WHERE id = :invoiceId")
    suspend fun getById(invoiceId: Long): SaleInvoiceEntity

    @Query("SELECT * FROM sale_invoice WHERE customer_id = :customerId")
    fun getByCustomerId(customerId: Long): Flow<List<SaleInvoiceEntity>>

    @Query("SELECT * FROM sale_invoice WHERE status = :status")
    fun getByStatus(status: String): Flow<List<SaleInvoiceEntity>>

    @Query("SELECT * FROM sale_invoice WHERE created_at = :createdAt")
    fun getByCreatedAt(createdAt: Long): Flow<List<SaleInvoiceEntity>>

    // --- 更多查询 ---

    @Query("SELECT * FROM sale_invoice ORDER BY created_at DESC")
    fun getAllForFlow(): Flow<List<SaleInvoiceEntity>>

    @Query("SELECT * FROM sale_invoice WHERE sn = :sn ORDER BY created_at DESC")
    fun getAllBySnForFlow(sn: String): Flow<List<SaleInvoiceEntity>>

    @Query("SELECT * FROM sale_invoice ORDER BY created_at DESC")
    fun getAllForPaged(): PagingSource<Int,SaleInvoiceEntity>

    @Query("SELECT * FROM sale_invoice WHERE sn = :sn ORDER BY created_at DESC")
    fun getAllBySnForPaged(sn: String): PagingSource<Int,SaleInvoiceEntity>

}
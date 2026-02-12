package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.PurchaseInvoice
import kotlinx.coroutines.flow.Flow

/**
 * 采购账单数据访问对象 (DAO)
 * 负责提供与采购账单相关的数据库操作方法。
 */
@Dao
interface PurchaseInvoiceDao {

    // --- 增加 (Create) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: PurchaseInvoice): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bills: List<PurchaseInvoice>): List<Long>

    // --- 删除 (Delete) ---

    @Delete
    suspend fun delete(bill: PurchaseInvoice): Int

    @Query("DELETE FROM purchase_invoice WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM purchase_invoice WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<Long>): Int

    @Query("DELETE FROM purchase_invoice WHERE sn = :sn")
    suspend fun deleteBySn(sn: String): Int

    @Query("DELETE FROM purchase_invoice WHERE sn IN (:sns)")
    suspend fun deleteBySns(sns: List<String>): Int

    @Query("DELETE FROM purchase_invoice")
    suspend fun deleteAll():Int

    // --- 修改 (Update) ---

    @Update
    suspend fun update(bill: PurchaseInvoice): Int

    @Query("UPDATE purchase_invoice SET sn = :sn WHERE id = :id")
    suspend fun updateSn(id: Long, sn: String): Int

    // --- 查询 (Query) ---

    @Query("SELECT COUNT(*) FROM purchase_invoice")
    suspend fun getCount(): Int

    @Query("SELECT * FROM purchase_invoice ORDER BY created_at DESC")
    fun getAll(): Flow<List<PurchaseInvoice>>

    @Query("SELECT * FROM purchase_invoice WHERE id = :invoiceId")
    suspend fun getById(invoiceId: Long): PurchaseInvoice

    @Query("SELECT * FROM purchase_invoice WHERE order_id = :orderId")
    fun getByOrderId(orderId: Long): Flow<List<PurchaseInvoice>>

    @Query("SELECT * FROM purchase_invoice WHERE supplier_id = :supplierId")
    fun getBySupplierId(supplierId: Long): Flow<List<PurchaseInvoice>>

    @Query("SELECT * FROM purchase_invoice WHERE user_id = :userId")
    fun getByUserId(userId: Long): Flow<List<PurchaseInvoice>>

    @Query("SELECT * FROM purchase_invoice WHERE status = :status")
    fun getByStatus(status: String): Flow<List<PurchaseInvoice>>

    @Query("SELECT * FROM purchase_invoice WHERE created_at = :createdAt")
    fun getByCreatedAt(createdAt: Long): Flow<List<PurchaseInvoice>>

    // --- 更多查询 ---

    @Query("SELECT * FROM purchase_invoice ORDER BY created_at DESC")
    fun getAllForFlow(): Flow<List<PurchaseInvoice>>

    @Query("SELECT * FROM purchase_invoice WHERE sn = :sn ORDER BY created_at DESC")
    fun getAllBySnForFlow(sn: String): Flow<List<PurchaseInvoice>>


    @Query("SELECT * FROM purchase_invoice ORDER BY created_at DESC")
    fun getAllForPaged(): PagingSource<Int,PurchaseInvoice>

    @Query("SELECT * FROM purchase_invoice WHERE sn = :sn ORDER BY created_at DESC")
    fun getAllBySnForPaged(sn: String): PagingSource<Int,PurchaseInvoice>

}
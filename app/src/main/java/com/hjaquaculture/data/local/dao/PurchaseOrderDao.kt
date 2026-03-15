package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.PurchaseOrderEntity
import kotlinx.coroutines.flow.Flow

/**
 * 采购订单数据访问对象 (DAO)
 */
@Dao
interface PurchaseOrderDao {

    // --- 增加 (Create) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: PurchaseOrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<PurchaseOrderEntity>)

    // --- 删除 (Delete) ---

    @Delete
    suspend fun delete(order: PurchaseOrderEntity)

    @Query("DELETE FROM purchase_order")
    suspend fun deleteAll()

    // --- 修改 (Update) ---
    @Update
    suspend fun update(order: PurchaseOrderEntity)

    @Query("UPDATE purchase_order SET sn = :sn WHERE id = :id")
    suspend fun updateSn(id: Long, sn: String): Int

    // --- 查询 (Query) ---

    @Query("SELECT COUNT(*) FROM purchase_order")
    suspend fun getCount(): Int

    @Query("SELECT * FROM purchase_order ORDER BY created_at DESC")
    fun getAll(): Flow<List<PurchaseOrderEntity>>

    // --- 更多查询 ---

    @Query("SELECT * FROM purchase_order WHERE id = :orderId")
    fun getById(orderId: Long): Flow<PurchaseOrderEntity?>

    @Query("SELECT * FROM purchase_order WHERE invoice_id = :invoiceId")
    fun getByInvoiceId(invoiceId: Long): Flow<List<PurchaseOrderEntity>>

    @Query("SELECT * FROM purchase_order WHERE supplier_id = :supplierId ORDER BY created_at DESC")
    fun getBySupplier(supplierId: Long): Flow<List<PurchaseOrderEntity>>

    @Query("SELECT * FROM purchase_order ORDER BY created_at DESC")
    fun getPagingSource(): PagingSource<Int, PurchaseOrderEntity>

}

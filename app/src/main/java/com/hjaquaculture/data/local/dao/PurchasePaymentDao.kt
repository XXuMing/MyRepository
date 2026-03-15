package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.PurchasePaymentEntity
import kotlinx.coroutines.flow.Flow

/**
 * 采购付款数据访问对象 (DAO)
 */
@Dao
interface PurchasePaymentDao {

    // --- 增加 (Create) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: PurchasePaymentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(payments: List<PurchasePaymentEntity>)

    // --- 删除 (Delete) ---

    @Delete
    suspend fun delete(payment: PurchasePaymentEntity)

    @Query("DELETE FROM purchase_payment WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM purchase_payment")
    suspend fun deleteAll()

    // --- 修改 (Update) ---

    @Update
    suspend fun update(payment: PurchasePaymentEntity)

    @Query("UPDATE purchase_payment SET sn = :sn WHERE id = :id")
    suspend fun updateSn(id: Long, sn: String): Int

    // --- 查询 (Query) ---

    @Query("SELECT COUNT(*) FROM purchase_payment")
    suspend fun getCount(): Int

    @Query("SELECT * FROM purchase_payment WHERE invoice_id = :invoiceId ORDER BY payment_time DESC")
    fun getByInvoiceId(invoiceId: Long): Flow<List<PurchasePaymentEntity>>

    @Query("SELECT * FROM purchase_payment ORDER BY payment_time DESC")
    fun getPagingSource(): PagingSource<Int, PurchasePaymentEntity>

}

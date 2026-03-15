package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.SalePaymentEntity
import kotlinx.coroutines.flow.Flow

/**
 *销售付款数据访问对象 (DAO)
 */
@Dao
interface SalePaymentDao {

    // --- 增加 (Create) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: SalePaymentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(payments: List<SalePaymentEntity>)

    // --- 删除 (Delete) ---
    @Delete
    suspend fun delete(payment: SalePaymentEntity)

    @Query("DELETE FROM sale_payment WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM sale_payment")
    suspend fun deleteAll()

    // --- 修改 (Update) ---

    @Update
    suspend fun update(payment: SalePaymentEntity)

    @Query("UPDATE sale_payment SET sn = :sn WHERE id = :id")
    suspend fun updateSn(id: Long, sn: String): Int


    // --- 查询 (Query) ---

    @Query("SELECT COUNT(*) FROM sale_payment")
    suspend fun getCount(): Int

    @Query("SELECT * FROM sale_payment WHERE invoice_id = :invoiceId ORDER BY payment_time DESC")
    fun getByInvoiceId(invoiceId: Long): Flow<List<SalePaymentEntity>>

    @Query("SELECT * FROM sale_payment ORDER BY payment_time DESC")
    fun getPagingSource(): PagingSource<Int, SalePaymentEntity>

}

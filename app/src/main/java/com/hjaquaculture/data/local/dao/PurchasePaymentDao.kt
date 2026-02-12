package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.PurchasePayment
import kotlinx.coroutines.flow.Flow

/**
 * 采购付款数据访问对象 (DAO)
 */
@Dao
interface PurchasePaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: PurchasePayment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(payments: List<PurchasePayment>)

    @Update
    suspend fun update(payment: PurchasePayment)

    @Delete
    suspend fun delete(payment: PurchasePayment)

    @Query("UPDATE purchase_payment SET sn = :sn WHERE id = :id")
    suspend fun updateSn(id: Long, sn: String): Int

    @Query("DELETE FROM purchase_payment")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：根据采购账单ID获取其所有的付款记录，并按付款日期降序排序。
     * @param billId 采购账单的ID
     * @return 包含该账单所有付款记录的 Flow
     */
    @Query("SELECT * FROM purchase_payment WHERE invoice_id = :billId ORDER BY payment_time DESC")
    fun getPaymentsForBill(billId: Long): Flow<List<PurchasePayment>>

    /**
     * 为 Paging 3.0 提供分页数据源，获取所有付款记录。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM purchase_payment ORDER BY payment_time DESC")
    fun getPurchasePaymentsPagingSource(): PagingSource<Int, PurchasePayment>

    @Query("SELECT COUNT(*) FROM purchase_payment")
    suspend fun getCount(): Int
}

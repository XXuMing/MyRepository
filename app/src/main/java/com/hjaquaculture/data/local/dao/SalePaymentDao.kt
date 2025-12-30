package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.entity.SalePayment
import kotlinx.coroutines.flow.Flow

/**
 *销售付款数据访问对象 (DAO)
 */
@Dao
interface SalePaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: SalePayment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(payments: List<SalePayment>)

    @Update
    suspend fun update(payment: SalePayment)

    @Delete
    suspend fun delete(payment: SalePayment)

    @Query("DELETE FROM sale_payment")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：根据销售发票ID获取其所有的付款记录，并按付款日期降序排序。
     * @param invoiceId 销售发票的ID
     * @return 包含该发票所有付款记录的 Flow
     */
    @Query("SELECT * FROM sale_payment WHERE invoice_id = :invoiceId ORDER BY payment_time DESC")
    fun getPaymentsForSaleInvoice(invoiceId: Long): Flow<List<SalePayment>>

    /**
     * 为 Paging 3 提供分页数据源，获取所有付款记录。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM sale_payment ORDER BY payment_time DESC")
    fun getSalePaymentsPagingSource(): PagingSource<Int, SalePayment>
}

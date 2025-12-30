package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.entity.SaleInvoice
import kotlinx.coroutines.flow.Flow

/**
 * 销售发票数据访问对象 (DAO)
 */
@Dao
interface SaleInvoiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(invoice: SaleInvoice): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(invoices: List<SaleInvoice>)

    @Update
    suspend fun update(invoice: SaleInvoice)

    @Delete
    suspend fun delete(invoice: SaleInvoice)

    @Query("DELETE FROM sale_invoice")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：获取所有销售发票，并按开票日期降序排序。
     * @return 包含所有销售发票列表的 Flow
     */
    @Query("SELECT * FROM sale_invoice ORDER BY created_at DESC")
    fun getAllSaleInvoices(): Flow<List<SaleInvoice>>

    /**
     * 根据ID获取单个销售发票。
     * @param invoiceId 销售发票的ID
     * @return 返回包含单个销售发票的 Flow，如果不存在则为 null
     */
    @Query("SELECT * FROM sale_invoice WHERE id = :invoiceId")
    fun getSaleInvoiceById(invoiceId: Long): Flow<SaleInvoice?>

    /**
     * 为 Paging 3 提供分页数据源，获取所有销售发票。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM sale_invoice ORDER BY created_at DESC")
    fun getSaleInvoicesPagingSource(): PagingSource<Int, SaleInvoice>
}

package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.entity.PurchaseInvoice
import kotlinx.coroutines.flow.Flow

/**
 * 采购账单数据访问对象 (DAO)
 * 负责提供与采购账单相关的数据库操作方法。
 */
@Dao
interface PurchaseInvoiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: PurchaseInvoice): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bills: List<PurchaseInvoice>)

    @Update
    suspend fun update(bill: PurchaseInvoice)

    @Delete
    suspend fun delete(bill: PurchaseInvoice)

    @Query("DELETE FROM purchase_invoice")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：获取所有采购账单，并按账单日期降序排序。
     * @return 包含所有采购账单列表的 Flow
     */
    @Query("SELECT * FROM purchase_invoice ORDER BY created_at DESC")
    fun getAllPurchaseBills(): Flow<List<PurchaseInvoice>>

    /**
     * 根据ID获取单个采购账单。
     * @param billId 采购账单的ID
     * @return 返回包含单个采购账单的 Flow，如果不存在则为 null
     */
    @Query("SELECT * FROM purchase_invoice WHERE id = :billId")
    fun getPurchaseBillById(billId: Long): Flow<PurchaseInvoice?>

    /**
     * 为 Paging 3.0 提供分页数据源，获取所有采购账单,，并按账单日期降序排序。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM purchase_invoice ORDER BY created_at DESC")
    fun getPurchaseBillsPagingSource(): PagingSource<Int, PurchaseInvoice>
}

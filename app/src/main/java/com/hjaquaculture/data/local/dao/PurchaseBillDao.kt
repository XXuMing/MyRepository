package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.entity.PurchaseBill
import kotlinx.coroutines.flow.Flow

/**
 * 采购账单数据访问对象 (DAO)
 */
@Dao
interface PurchaseBillDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: PurchaseBill): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bills: List<PurchaseBill>)

    @Update
    suspend fun update(bill: PurchaseBill)

    @Delete
    suspend fun delete(bill: PurchaseBill)

    @Query("DELETE FROM purchase_bill")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：获取所有采购账单，并按账单日期降序排序。
     * @return 包含所有采购账单列表的 Flow
     */
    @Query("SELECT * FROM purchase_bill ORDER BY created_at DESC")
    fun getAllPurchaseBills(): Flow<List<PurchaseBill>>

    /**
     * 根据ID获取单个采购账单。
     * @param billId 采购账单的ID
     * @return 返回包含单个采购账单的 Flow，如果不存在则为 null
     */
    @Query("SELECT * FROM purchase_bill WHERE id = :billId")
    fun getPurchaseBillById(billId: Long): Flow<PurchaseBill?>

    /**
     * 为 Paging 3.0 提供分页数据源，获取所有采购账单,，并按账单日期降序排序。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM purchase_bill ORDER BY created_at DESC")
    fun getPurchaseBillsPagingSource(): PagingSource<Int, PurchaseBill>
}

package com.hjaquaculture.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.PurchaseOrderItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * 采购订单项数据访问对象 (DAO)
 */
@Dao
interface PurchaseOrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PurchaseOrderItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PurchaseOrderItemEntity>)

    @Update
    suspend fun update(item: PurchaseOrderItemEntity)

    @Delete
    suspend fun delete(item: PurchaseOrderItemEntity)

    @Query("DELETE FROM purchase_order_item")
    suspend fun deleteAll()

    @Query("SELECT * FROM purchase_order_item WHERE order_id = :orderId ORDER BY created_at DESC")
    fun getForPurchaseOrder(orderId: Long): Flow<List<PurchaseOrderItemEntity>>

    @Query("SELECT COUNT(*) FROM purchase_order_item")
    suspend fun getCount(): Int
}

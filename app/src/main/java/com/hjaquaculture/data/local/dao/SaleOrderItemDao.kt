package com.hjaquaculture.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.SaleOrderItemEntity
import kotlinx.coroutines.flow.Flow

/**
 * 销售订单项数据访问对象 (DAO)
 */
@Dao
interface SaleOrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SaleOrderItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SaleOrderItemEntity>)

    @Update
    suspend fun update(item: SaleOrderItemEntity)

    @Delete
    suspend fun delete(item: SaleOrderItemEntity)

    @Query("DELETE FROM sale_order_item")
    suspend fun deleteAll()

    @Query("SELECT * FROM sale_order_item WHERE order_id = :orderId ORDER BY created_at DESC")
    fun getForSaleOrder(orderId: Long): Flow<List<SaleOrderItemEntity>>

    @Query("SELECT COUNT(*) FROM sale_order_item")
    suspend fun getCount(): Int

}
package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.entity.SaleOrderItem
import kotlinx.coroutines.flow.Flow

/**
 * 销售订单项数据访问对象 (DAO)
 */
@Dao
interface SaleOrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SaleOrderItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SaleOrderItem>)

    @Update
    suspend fun update(item: SaleOrderItem)

    @Delete
    suspend fun delete(item: SaleOrderItem)

    @Query("DELETE FROM sale_order_item")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：根据销售订单ID获取其所有的订单项。
     * @param orderId 销售订单的ID
     * @return 包含该订单所有订单项的 Flow
     */
    @Query("SELECT * FROM sale_order_item WHERE order_id = :orderId")
    fun getItemsForSaleOrder(orderId: Long): Flow<List<SaleOrderItem>>

    /**
     * 为 Paging 3 提供分页数据源，获取所有销售订单项。
     * (注意：通常按订单ID查询更有意义，此处提供一个全局分页的示例)
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM sale_order_item ORDER BY id DESC")
    fun getSaleOrderItemsPagingSource(): PagingSource<Int, SaleOrderItem>
}

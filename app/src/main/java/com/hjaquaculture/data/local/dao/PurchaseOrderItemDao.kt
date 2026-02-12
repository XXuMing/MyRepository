package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.PurchaseOrderItem
import kotlinx.coroutines.flow.Flow

/**
 * 采购订单项数据访问对象 (DAO)
 */
@Dao
interface PurchaseOrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PurchaseOrderItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PurchaseOrderItem>)

    @Update
    suspend fun update(item: PurchaseOrderItem)

    @Delete
    suspend fun delete(item: PurchaseOrderItem)

    @Query("DELETE FROM purchase_order_item")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：根据采购订单ID获取其所有的订单项。
     * @param orderId 采购订单的ID
     * @return 包含该订单所有订单项的 Flow
     */
    @Query("SELECT * FROM purchase_order_item WHERE order_id = :orderId")
    fun getItemsForPurchaseOrder(orderId: Long): Flow<List<PurchaseOrderItem>>

    /**
     * 为 Paging 3.0 提供分页数据源，获取所有采购订单项。
     * （注意：通常按订单ID查询更有意义，此处提供一个全局分页的示例）
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM purchase_order_item ORDER BY order_id DESC")
    fun getPurchaseOrderItemsPagingSource(): PagingSource<Int, PurchaseOrderItem>


    @Query("SELECT COUNT(*) FROM purchase_order_item")
    suspend fun getCount(): Int
}

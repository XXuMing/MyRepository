package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.PurchaseOrder
import kotlinx.coroutines.flow.Flow

/**
 * 采购订单数据访问对象 (DAO)
 */
@Dao
interface PurchaseOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: PurchaseOrder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<PurchaseOrder>)

    @Update
    suspend fun update(order: PurchaseOrder)

    @Delete
    suspend fun delete(order: PurchaseOrder)

    @Query("UPDATE purchase_order SET sn = :sn WHERE id = :id")
    suspend fun updateSn(id: Long, sn: String): Int

    @Query("DELETE FROM purchase_order")
    suspend fun deleteAll()

    /**
     * 根据ID获取单个采购订单。
     * @param orderId 订单的ID
     * @return 返回包含单个订单的 Flow
     */
    @Query("SELECT * FROM purchase_order WHERE id = :orderId")
    fun getOrderById(orderId: Long): Flow<PurchaseOrder?>

    /**
     * 获取所有采购订单，按日期降序排列。
     * @return 包含所有订单列表的 Flow
     */
    @Query("SELECT * FROM purchase_order ORDER BY order_date DESC")
    fun getAllOrders(): Flow<List<PurchaseOrder>>

    /**
     * 根据供应商ID获取其所有采购订单。
     * @param supplierId 供应商的ID
     * @return 该供应商的所有订单列表 Flow
     */
    @Query("SELECT * FROM purchase_order WHERE supplier_id = :supplierId ORDER BY order_date DESC")
    fun getOrdersBySupplier(supplierId: Long): Flow<List<PurchaseOrder>>

    /**
     * 为 Paging 3.0 提供分页数据源。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM purchase_order ORDER BY order_date DESC")
    fun getOrdersPagingSource(): PagingSource<Int, PurchaseOrder>


    @Query("SELECT COUNT(*) FROM purchase_order")
    suspend fun getCount(): Int
}

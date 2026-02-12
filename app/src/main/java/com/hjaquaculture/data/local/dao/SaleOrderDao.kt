package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.SaleOrder
import kotlinx.coroutines.flow.Flow

/**
 * 销售订单数据访问对象 (DAO)
 */
@Dao
interface SaleOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: SaleOrder): Long

    /**
     * 更新订单编号
     * @param id 订单ID
     * @param orderSn 新的订单编号
     */
    @Query("UPDATE sale_order SET sn = :orderSn WHERE id = :id")
    suspend fun updateSn(id: Long, orderSn: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<SaleOrder>)

    @Update
    suspend fun update(order: SaleOrder)

    @Delete
    suspend fun delete(order: SaleOrder)

    @Query("DELETE FROM sale_order")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：获取所有销售订单，并按订单日期降序排序。
     * @return 包含所有销售订单列表的 Flow
     */
    @Query("SELECT * FROM sale_order ORDER BY created_at DESC")
    fun getAllSaleOrders(): Flow<List<SaleOrder>>

    /**
     * 根据ID获取单个销售订单。
     * @param orderId 销售订单的ID
     * @return 返回包含单个销售订单的 Flow，如果不存在则为 null
     */
    @Query("SELECT * FROM sale_order WHERE id = :orderId")
    fun getSaleOrderById(orderId: Long): Flow<SaleOrder?>

    /**
     * 为 Paging 3 提供分页数据源，获取所有销售订单。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM sale_order ORDER BY created_at DESC")
    fun getSaleOrdersPagingSource(): PagingSource<Int, SaleOrder>

    @Query("SELECT COUNT(*) FROM sale_order")
    suspend fun getCount(): Int
}

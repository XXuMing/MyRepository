package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.common.base.OrderStatus
import com.hjaquaculture.data.local.entity.SaleOrderEntity
import kotlinx.coroutines.flow.Flow

/**
 * 销售订单数据访问对象 (DAO)
 */
@Dao
interface SaleOrderDao {

    // --- 增加 (Create) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: SaleOrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<SaleOrderEntity>)

    // --- 删除 (Delete) ---

    @Delete
    suspend fun delete(order: SaleOrderEntity)

    @Query("DELETE FROM sale_order")
    suspend fun deleteAll()

    // --- 修改 (Update) ---

    @Update
    suspend fun update(order: SaleOrderEntity)

    @Query("UPDATE sale_order SET sn = :orderSn WHERE id = :id")
    suspend fun updateSn(id: Long, orderSn: String): Int

    // --- 查询 (Query) ---

    @Query("SELECT COUNT(*) FROM sale_order")
    suspend fun getCount(): Int

    @Query("SELECT * FROM sale_order ORDER BY created_at DESC")
    fun getAll(): Flow<List<SaleOrderEntity>>

    // --- 更多查询 ---

    @Query("SELECT * FROM sale_order WHERE id = :orderId")
    fun getById(orderId: Long): Flow<SaleOrderEntity?>

    @Query("SELECT * FROM sale_order WHERE invoice_id = :invoiceId")
    fun getByInvoiceId(invoiceId: Long): Flow<List<SaleOrderEntity>>

    @Query("SELECT * FROM sale_order WHERE customer_id = :customerId ORDER BY created_at DESC")
    fun getByCustomer(customerId: Long): Flow<List<SaleOrderEntity>>

    @Query("SELECT * FROM sale_order WHERE order_status = :status ORDER BY created_at DESC")
    fun getByStatus(status: OrderStatus): Flow<List<SaleOrderEntity>>

    @Query("""
        SELECT * FROM sale_order 
        WHERE (:customerId IS NULL OR customer_id = :customerId)
        AND (:type IS NULL OR order_type = :type)
        AND (:status IS NULL OR order_status = :status)
        AND (:sn IS NULL OR sn = :sn)
    """)
    fun getPagingSource(
        sn: String,
        customerId: Long? = null,
        type: String?,
        status: OrderStatus? = null
    ): PagingSource<Int, SaleOrderEntity>

}

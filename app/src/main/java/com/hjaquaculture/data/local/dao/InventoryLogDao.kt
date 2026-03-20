package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hjaquaculture.common.base.StockChangeType
import com.hjaquaculture.data.local.entity.InventoryLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryLogDao {

    // --- 增加 ---

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(log: InventoryLogEntity): Long

    // --- 查询 ---

    // 某商品的完整变动历史，按时间倒序
    @Query("SELECT * FROM inventory_log WHERE product_id = :productId ORDER BY created_at DESC")
    fun getByProductId(productId: Long): Flow<List<InventoryLogEntity>>

    // 获取最新快照（用于计算下一条记录的 balanceSnapshot）
    @Query("SELECT balance_snapshot FROM inventory_log WHERE product_id = :productId ORDER BY created_at DESC LIMIT 1")
    suspend fun getLatestSnapshot(productId: Long): Int?

    // 某商品某段时间内的变动
    @Query("""
        SELECT * FROM inventory_log 
        WHERE product_id = :productId 
        AND created_at BETWEEN :startAt AND :endAt 
        ORDER BY created_at DESC
    """)
    fun getByProductIdAndTimeRange(
        productId: Long,
        startAt: Long,
        endAt: Long
    ): Flow<List<InventoryLogEntity>>

    // 按变动类型查询（如只看损耗记录）
    @Query("SELECT * FROM inventory_log WHERE change_type = :changeType ORDER BY created_at DESC")
    fun getByChangeType(changeType: StockChangeType): Flow<List<InventoryLogEntity>>

    // 分页查询（列表展示用）
    @Query("SELECT * FROM inventory_log WHERE product_id = :productId ORDER BY created_at DESC")
    fun getPagingSource(productId: Long): PagingSource<Int, InventoryLogEntity>

    @Query("SELECT COUNT(*) FROM inventory_log")
    suspend fun getCount(): Int
}
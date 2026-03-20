package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hjaquaculture.data.local.entity.StocktakingDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StocktakingDetailDao {

    // --- 增加 ---

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: StocktakingDetailEntity): Long

    // 批量插入（发起盘点时，一次性生成所有商品的明细）
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(items: List<StocktakingDetailEntity>)

    // --- 修改 ---

    // 录入实际数量，同时计算差异
    @Query("""
        UPDATE stocktaking_item 
        SET actual_amount = :actualAmount,
            difference = :actualAmount - system_amount
        WHERE stocktaking_id = :stocktakingId 
        AND product_id = :productId
    """)
    suspend fun updateActualAmount(
        stocktakingId: Long,
        productId: Long,
        actualAmount: Int
    ): Int

    // --- 查询 ---

    @Query("SELECT * FROM stocktaking_item WHERE stocktaking_id = :stocktakingId")
    fun getByStocktakingId(stocktakingId: Long): Flow<List<StocktakingDetailEntity>>

    // 尚未录入的明细
    @Query("""
        SELECT * FROM stocktaking_item 
        WHERE stocktaking_id = :stocktakingId 
        AND actual_amount IS NULL
    """)
    fun getPendingItems(stocktakingId: Long): Flow<List<StocktakingDetailEntity>>

    // 有差异的明细（盘点确认时使用）
    @Query("""
        SELECT * FROM stocktaking_item 
        WHERE stocktaking_id = :stocktakingId 
        AND difference != 0
    """)
    suspend fun getDifferentItems(stocktakingId: Long): List<StocktakingDetailEntity>

    // 盘盈明细
    @Query("""
        SELECT * FROM stocktaking_item 
        WHERE stocktaking_id = :stocktakingId 
        AND difference > 0
    """)
    suspend fun getSurplusItems(stocktakingId: Long): List<StocktakingDetailEntity>

    // 盘亏明细
    @Query("""
        SELECT * FROM stocktaking_item 
        WHERE stocktaking_id = :stocktakingId 
        AND difference < 0
    """)
    suspend fun getDeficitItems(stocktakingId: Long): List<StocktakingDetailEntity>

    // 统计未录入数量（用于 UI 提示"还有X个商品未盘点"）
    @Query("""
        SELECT COUNT(*) FROM stocktaking_item 
        WHERE stocktaking_id = :stocktakingId 
        AND actual_amount IS NULL
    """)
    fun getPendingCount(stocktakingId: Long): Flow<Int>

    // 分页查询（盘点明细列表展示）
    @Query("""
        SELECT * FROM stocktaking_item 
        WHERE stocktaking_id = :stocktakingId
    """)
    fun getPagingSource(stocktakingId: Long): PagingSource<Int, StocktakingDetailEntity>

    @Query("SELECT COUNT(*) FROM stocktaking_item WHERE stocktaking_id = :stocktakingId")
    suspend fun getCountByStocktakingId(stocktakingId: Long): Int
}
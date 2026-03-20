package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hjaquaculture.common.base.StocktakingStatus
import com.hjaquaculture.data.local.entity.StocktakingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StocktakingDao {

    // --- 增加 ---

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(stocktaking: StocktakingEntity): Long

    // --- 修改 ---

    @Query("""
        UPDATE stocktaking 
        SET status = :status, completed_at = :completedAt 
        WHERE id = :id
    """)
    suspend fun updateStatus(
        id: Long,
        status: StocktakingStatus,
        completedAt: Long? = null
    ): Int

    // --- 查询 ---

    @Query("SELECT * FROM stocktaking WHERE id = :id")
    fun getById(id: Long): Flow<StocktakingEntity?>

    @Query("SELECT * FROM stocktaking ORDER BY created_at DESC")
    fun getAll(): Flow<List<StocktakingEntity>>

    @Query("SELECT * FROM stocktaking WHERE status = :status ORDER BY created_at DESC")
    fun getByStatus(status: StocktakingStatus): Flow<List<StocktakingEntity>>

    @Query("SELECT * FROM stocktaking ORDER BY created_at DESC")
    fun getPagingSource(): PagingSource<Int, StocktakingEntity>

    // 查询是否有进行中的盘点（同一时间只允许一张盘点单进行）
    @Query("SELECT COUNT(*) FROM stocktaking WHERE status = 'IN_PROGRESS'")
    suspend fun getInProgressCount(): Int

    @Query("SELECT COUNT(*) FROM stocktaking")
    suspend fun getCount(): Int
}
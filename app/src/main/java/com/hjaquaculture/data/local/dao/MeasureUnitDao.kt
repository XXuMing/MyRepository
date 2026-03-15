package com.hjaquaculture.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.entity.MeasureUnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasureUnitDao {

    // --- 插入 (Create) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(unitEntity: MeasureUnitEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(unitEntities: List<MeasureUnitEntity>)

    // --- 更新 (Update) ---

    @Update
    suspend fun update(unitEntity: MeasureUnitEntity): Int

    // --- 删除 (Delete) ---

    @Delete
    suspend fun delete(unitEntity: MeasureUnitEntity): Int

    @Query("DELETE FROM measure_units")
    suspend fun deleteAll()

    // --- 查询 (Read) ---

    @Query("SELECT * FROM measure_units")
    fun getAll(): Flow<List<MeasureUnitEntity>>

    @Query("SELECT * FROM measure_units WHERE id = :id")
    suspend fun getById(id: Int): MeasureUnitEntity?

    @Query("SELECT COUNT(*) FROM measure_units")
    suspend fun getCount(): Int
}
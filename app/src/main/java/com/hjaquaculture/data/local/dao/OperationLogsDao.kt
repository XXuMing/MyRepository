package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hjaquaculture.data.local.model.entity.OperationLogs
import kotlinx.coroutines.flow.Flow

/**
 * 操作记录数据访问对象 (DAO)
 */
@Dao
interface OperationLogsDao {

    /**
     * 插入一条新的操作记录。
     * @param log 要插入的操作记录对象
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: OperationLogs)

    /**
     * 插入一个操作记录列表。
     * @param logs 操作记录列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<OperationLogs>)

    /**
     * 【推荐】响应式查询：获取所有操作记录，并按事件时间降序排序。
     * @return 包含所有操作记录列表的 Flow
     */
    @Query("SELECT * FROM operation_logs ORDER BY eventTime DESC")
    fun getAllLogs(): Flow<List<OperationLogs>>

    /**
     * 响应式查询：根据用户ID获取其所有操作记录。
     * @param userId 用户ID
     * @return 包含该用户所有操作记录的 Flow
     */
    @Query("SELECT * FROM operation_logs WHERE user_id = :userId ORDER BY eventTime DESC")
    fun getLogsByUserId(userId: Long): Flow<List<OperationLogs>>

    /**
     * 响应式查询：根据模块名称获取相关的所有操作记录。
     * @param module 模块名称
     * @return 包含该模块所有操作记录的 Flow
     */
    @Query("SELECT * FROM operation_logs WHERE module = :module ORDER BY eventTime DESC")
    fun getLogsByModule(module: String): Flow<List<OperationLogs>>

    /**
     * 为 Paging 3 提供分页数据源，获取所有操作记录。
     * 这是最常用的分页查询。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM operation_logs ORDER BY eventTime DESC")
    fun getLogsPagingSource(): PagingSource<Int, OperationLogs>

    /**
     * 为 Paging 3 提供分页数据源，根据用户ID筛选操作记录。
     * @param userId 用户ID
     * @return 返回该用户操作记录的 PagingSource
     */
    @Query("SELECT * FROM operation_logs WHERE user_id = :userId ORDER BY eventTime DESC")
    fun getLogsPagingSourceByUserId(userId: Long): PagingSource<Int, OperationLogs>

    /**
     * 为 Paging 3 提供分页数据源，根据模块名称筛选操作记录。
     * @param module 模块名称
     * @return 返回该模块操作记录的 PagingSource
     */
    @Query("SELECT * FROM operation_logs WHERE module LIKE :module ORDER BY eventTime DESC")
    fun getLogsPagingSourceByModule(module: String): PagingSource<Int, OperationLogs>

    /**
     * 删除所有操作记录。
     * （请谨慎使用此操作）
     */
    @Query("DELETE FROM operation_logs")
    suspend fun deleteAll()
}

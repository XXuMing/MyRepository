package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.entity.Supplier
import kotlinx.coroutines.flow.Flow

/**
 * 供应商数据访问对象 (DAO)
 */
@Dao
interface SupplierDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(supplier: Supplier): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(suppliers: List<Supplier>)

    @Update
    suspend fun update(supplier: Supplier)

    @Delete
    suspend fun delete(supplier: Supplier)

    @Query("DELETE FROM supplier")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：获取所有供应商，并按名称排序。
     * @return 包含所有供应商列表的 Flow
     */
    @Query("SELECT * FROM supplier ORDER BY name ASC")
    fun getAllSuppliers(): Flow<List<Supplier>>

    /**
     * 根据ID获取单个供应商。
     * @param supplierId 供应商的ID
     * @return 返回包含单个供应商的 Flow，如果不存在则为 null
     */
    @Query("SELECT * FROM supplier WHERE id = :supplierId")
    fun getSupplierById(supplierId: Long): Flow<Supplier?>

    /**
     * 为 Paging 3 提供分页数据源，获取所有供应商。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM supplier ORDER BY name ASC")
    fun getSuppliersPagingSource(): PagingSource<Int, Supplier>
}

package com.hjaquaculture.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hjaquaculture.data.local.entity.ProductEntity
import com.hjaquaculture.data.local.entity.ProductVarietyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductVarietyDao{

    // --- 增加 (Create) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: ProductVarietyEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<ProductVarietyEntity>): List<Long>

    // --- 删除 (Delete) ---

    @Delete
    suspend fun delete(category: ProductVarietyEntity): Int

    @Query("DELETE FROM product_variety WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM product_variety")
    suspend fun deleteAll(): Int

    // --- 修改 (Update) ---
    @Update
    suspend fun update(category: ProductVarietyEntity): Int

    @Query("UPDATE product_variety SET name = :newName WHERE id = :id")
    suspend fun update(id: Long, newName: String): Int

    // 批量更新分类
    // Room 会根据传入对象的 PrimaryKey (id) 自动找到对应的行并更新 sort 字段
    @Update
    suspend fun updateCategories(categories: List<ProductVarietyEntity>)

    // 开启事务进行批量更新（可选，但推荐）
    @Transaction
    suspend fun updateSortOrder(categoriesWithNewSort: List<ProductVarietyEntity>) {
        updateCategories(categoriesWithNewSort)
    }

    // --- 查询 (Query) ---

    /**
     * 获取分类总数
     */
    @Query("SELECT COUNT(*) FROM product_variety")
    suspend fun getCount(): Int

    /**
     * 获取所有分类，并按照 sort 字段升序排列
     * 使用 Flow 可以实时监听数据库变化（响应式）
     */
    @Query("SELECT * FROM product_variety ORDER BY sort ASC")
    fun getAll(): Flow<List<ProductVarietyEntity>>

    /**
     * 根据 ID 获取单个分类
     */
    @Query("SELECT * FROM product_variety WHERE id = :id")
    suspend fun getById(id: Long): ProductVarietyEntity


    /**
     * 根据名称模糊查询分类
     */
    @Query("SELECT * FROM product_variety WHERE name LIKE '%' || :name || '%' ORDER BY sort ASC")
    fun getByName(name: String): Flow<List<ProductVarietyEntity>>

    @Transaction
    @Query("""
        SELECT * 
        FROM product_variety AS pc
        INNER JOIN product AS p ON p.variety_id = pc.id
        ORDER BY pc.sort ASC , p.sort ASC
    """)
    fun getAllCategoriesWithProducts(): Flow<Map<ProductVarietyEntity, List<ProductEntity>>>
}
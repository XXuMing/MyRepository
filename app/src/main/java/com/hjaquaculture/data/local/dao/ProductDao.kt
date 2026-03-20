package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
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
interface ProductDao {

    // --- 增加 (Create) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>): List<Long>

    // --- 删除 (Delete) ---

    @Delete
    suspend fun delete(product: ProductEntity): Int

    @Query("DELETE FROM product WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM product")
    suspend fun deleteAll(): Int

    // --- 修改 (Update) ---

    @Update
    suspend fun update(product: ProductEntity): Int

    // --- 查询 (Query) ---
    @Query("SELECT COUNT(*) FROM product")
    suspend fun getCount(): Int

    /**
     * 根据ID获取单个商品。
     * @param productId 商品的ID
     * @return 返回包含单个商品的 Flow，如果不存在则为 null
     */
    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getById(productId: Long): ProductEntity

    /**
     * 根据分类获取所有商品。
     * @param categoryId 分类ID
     * @return 返回该分类下的所有商品列表 Flow
     */
    @Query("SELECT * FROM product WHERE variety_id = :categoryId ORDER BY sort ASC")
    suspend fun getByCategoryId(categoryId: Long): List<ProductEntity>

    /**
     * 【推荐】响应式查询：获取所有商品，并按分类和排序字段排序。
     * @return 包含所有商品列表的 Flow
     */
    @Query("SELECT * FROM product ORDER BY variety_id ASC, sort ASC")
    fun getAll(): Flow<List<ProductEntity>>


    /**
     * 根据商品名称模糊搜索商品。
     * @param name 搜索的关键词
     * @return 返回符合条件的商品列表 Flow
     */
    @Query("""
        SELECT * FROM product 
        WHERE name LIKE '%' || :name || '%' 
        ORDER BY variety_id ASC, sort ASC
        """)
    fun getByName(name: String): Flow<List<ProductEntity>>

    // --- 更多查询 ---
    /**
     * 为 Paging 3.0 提供分页数据源。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM product ORDER BY variety_id ASC, sort ASC")
    fun getAllForPagingSource(): PagingSource<Int, ProductEntity>

    @Transaction
    @Query("""
        SELECT * FROM product 
        ORDER BY variety_id ASC, sort ASC
    """)
    fun getProductsWithCategoryPaged(): Flow<Map<ProductVarietyEntity, List<ProductEntity>>>
}

package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.entity.Product
import kotlinx.coroutines.flow.Flow

/**
 * 商品数据访问对象 (DAO)
 *
 * 提供了与 'products' 表进行交互的所有方法。
 */
@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM product")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：获取所有商品，并按分类和排序字段排序。
     * @return 包含所有商品列表的 Flow
     */
    @Query("SELECT * FROM product ORDER BY category ASC, sort ASC")
    fun getAllProducts(): Flow<List<Product>>

    /**
     * 根据ID获取单个商品。
     * @param productId 商品的ID
     * @return 返回包含单个商品的 Flow，如果不存在则为 null
     */
    @Query("SELECT * FROM product WHERE id = :productId")
    fun getProductById(productId: Long): Flow<Product?>

    /**
     * 根据商品名称模糊搜索商品。
     * @param name 搜索的关键词
     * @return 返回符合条件的商品列表 Flow
     */
    @Query("SELECT * FROM product WHERE name LIKE '%' || :name || '%' ORDER BY category ASC, sort ASC")
    fun findProductsByName(name: String): Flow<List<Product>>

    /**
     * 根据分类获取所有商品。
     * @param categoryName 分类名称
     * @return 返回该分类下的所有商品列表 Flow
     */
    @Query("SELECT * FROM product WHERE category = :categoryName ORDER BY sort ASC")
    fun getProductsByCategory(categoryName: String): Flow<List<Product>>

    /**
     * 为 Paging 3.0 提供分页数据源。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM product ORDER BY category ASC, sort ASC")
    fun getProductsPagingSource(): PagingSource<Int, Product>
}

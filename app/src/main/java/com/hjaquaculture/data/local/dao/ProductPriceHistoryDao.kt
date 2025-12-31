package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.entity.ProductPriceHistory
import kotlinx.coroutines.flow.Flow

/**
 * 商品价格历史数据访问对象 (DAO)
 */
@Dao
interface ProductPriceHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(priceHistory: ProductPriceHistory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(priceHistories: List<ProductPriceHistory>)

    // 使用 OnConflictStrategy.REPLACE
    // 如果日期和商品ID冲突了，Room会自动覆盖旧价格，实现“每日最新价”
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePrice(priceHistory: ProductPriceHistory)

    @Update
    suspend fun update(priceHistory: ProductPriceHistory)

    @Delete
    suspend fun delete(priceHistory: ProductPriceHistory)

    @Query("DELETE FROM product_price_history")
    suspend fun deleteAll()

    /**
     * 【推荐】响应式查询：根据商品ID获取其所有的价格历史记录，并按日期降序排序。
     * @param productId 商品的ID
     * @return 包含该商品所有价格历史记录的 Flow
     */
    @Query("SELECT * FROM product_price_history WHERE product_id = :productId ORDER BY created_at DESC")
    fun getPriceHistoryForProduct(productId: Long): Flow<List<ProductPriceHistory>>

    /**
     * 为 Paging 3.0 提供分页数据源，获取所有价格历史记录。
     * @return 返回 PagingSource
     */
    @Query("SELECT * FROM product_price_history ORDER BY created_at DESC")
    fun getPriceHistoryPagingSource(): PagingSource<Int, ProductPriceHistory>
}

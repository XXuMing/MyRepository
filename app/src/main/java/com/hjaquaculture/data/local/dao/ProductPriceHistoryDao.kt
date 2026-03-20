package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hjaquaculture.data.local.entity.ProductPriceHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductPriceHistoryDao {

    // --- 增加 ---

    // 价格历史是审计记录，只能新增，不能覆盖
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(priceHistory: ProductPriceHistoryEntity): Long

    // --- 删除 ---

    // 通常不应该删除价格历史，但保留清空方法用于测试环境
    @Query("DELETE FROM product_price_history")
    suspend fun deleteAll()

    // --- 查询 ---

    // 某商品的完整价格历史，按时间倒序
    @Query("SELECT * FROM product_price_history WHERE product_id = :productId ORDER BY changed_at DESC")
    fun getByProductId(productId: Long): Flow<List<ProductPriceHistoryEntity>>

    // 获取最新一条（当前价格）
    @Query("""
        SELECT * FROM product_price_history 
        WHERE product_id = :productId 
        ORDER BY changed_at DESC 
        LIMIT 1
    """)
    suspend fun getLatest(productId: Long): ProductPriceHistoryEntity?

    // 获取最新两条（用于推导"原价 → 新价"的调价事件）
    // 第一条是当前价，第二条是上一次的价格
    @Query("""
        SELECT * FROM product_price_history 
        WHERE product_id = :productId 
        ORDER BY changed_at DESC 
        LIMIT 2
    """)
    suspend fun getLatestTwo(productId: Long): List<ProductPriceHistoryEntity>

    // 某段时间内所有商品的调价记录（用于报表）
    @Query("""
        SELECT * FROM product_price_history 
        WHERE changed_at BETWEEN :startAt AND :endAt 
        ORDER BY changed_at DESC
    """)
    fun getByTimeRange(startAt: Long, endAt: Long): Flow<List<ProductPriceHistoryEntity>>

    // 某操作员的调价记录（用于审计）
    @Query("""
        SELECT * FROM product_price_history 
        WHERE operator_id = :operatorId 
        ORDER BY changed_at DESC
    """)
    fun getByOperatorId(operatorId: Long): Flow<List<ProductPriceHistoryEntity>>

    // 分页查询（某商品的价格历史列表展示用）
    @Query("""
        SELECT * FROM product_price_history 
        WHERE product_id = :productId 
        ORDER BY changed_at DESC
    """)
    fun getPagingSource(productId: Long): PagingSource<Int, ProductPriceHistoryEntity>

    @Query("SELECT COUNT(*) FROM product_price_history")
    suspend fun getCount(): Int
}

/**
为什么去掉 update 和 delete 单条记录的方法：
价格历史是审计数据，它的价值在于"不可篡改"。如果提供了修改和删除单条记录的方法，审计意义就丧失了。Repository 层应该只暴露 insert，让调用方无法绕过这个约束。
getLatest 和 getLatestTwo 都是 suspend 而不是 Flow：
这两个查询通常用于业务逻辑计算（比如开单时展示当前价格、调价时对比前后价格），是一次性读取，不需要实时监听变化，所以用 suspend。
getByProductId 是 Flow：
用于 UI 层展示某商品的完整调价历史列表，数据变化时需要自动刷新，所以用 Flow。
 */
package com.hjaquaculture.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.hjaquaculture.data.local.entity.ProductInventoryView
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductInventoryDao {

    @Query("SELECT * FROM product_inventory_view ORDER BY variety_id ASC, sort ASC")
    fun getAll(): Flow<List<ProductInventoryView>>

    @Query("SELECT * FROM product_inventory_view WHERE id = :productId")
    fun getById(productId: Long): Flow<ProductInventoryView?>

    @Query("SELECT * FROM product_inventory_view WHERE variety_id = :varietyId ORDER BY sort ASC")
    fun getByVariety(varietyId: Long): Flow<List<ProductInventoryView>>

    // 只查有库存预警的商品
    @Query("""
        SELECT * FROM product_inventory_view 
        WHERE stock_status IN ('NEGATIVE', 'LOW')
        ORDER BY stock_amount ASC
    """)
    fun getWarningItems(): Flow<List<ProductInventoryView>>

    // 搜索
    @Query("""
        SELECT * FROM product_inventory_view 
        WHERE name LIKE '%' || :query || '%'
        ORDER BY variety_id ASC, sort ASC
    """)
    fun search(query: String): Flow<List<ProductInventoryView>>

    // 分页
    @Query("SELECT * FROM product_inventory_view ORDER BY variety_id ASC, sort ASC")
    fun getPagingSource(): PagingSource<Int, ProductInventoryView>
}
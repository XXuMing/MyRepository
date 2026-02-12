package com.hjaquaculture.data.local.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.hjaquaculture.data.local.entity.ProductCategory
import com.hjaquaculture.data.local.entity.ProductPriceHistory

data class PriceHistory(
    @Embedded
    val history: ProductPriceHistory,

    @Relation(
        parentColumn = "product_category_id",
        entityColumn = "id" // 假设 Category 实体的初级键是 id
    )
    val category: ProductCategory? // 这样就能拿到分类名称了
)

data class PriceHistoryWithCategory(
    val id: Long,
    val productId: Long,
    val newPrice: Int,
    val newPriceDate: String,
    val categoryName: String, // 从 Category 表联查
    val categoryId: Long      // 从 Product 表联查
)
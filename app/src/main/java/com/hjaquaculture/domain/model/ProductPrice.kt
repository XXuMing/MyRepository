package com.hjaquaculture.domain.model

import com.hjaquaculture.data.local.entity.ProductPriceHistory

data class PriceHistoryScreenData(
    val dateGroups: List<DateGroup>
)

data class DateGroup(
    val date: String,
    val categoryGroups: List<CategoryGroup>
)

data class CategoryGroup(
    val categoryId: Long,
    val categoryName: String, // 建议在查询时 Join 获取名称
    val items: List<ProductPriceHistory>
)
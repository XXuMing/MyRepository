package com.hjaquaculture.feature.product

import com.hjaquaculture.data.local.entity.Product
import com.hjaquaculture.data.local.entity.ProductCategory

/**
 * 分类和商品的视图对象
 */
data class CategoryWithProductsVO(

    /**
     * 分类对象
     */
    val category: ProductCategory,

    /**
     * 商品列表
     */
    val products: List<Product> = emptyList(),

    /**
     * 是否展开子表
     */
    val isExpanded: Boolean = false,

    /**
     * 是否正在查询子表
     */
    val isLoadingProducts: Boolean = false,

    /**
     * 是否为刚创建需要自动聚焦
     */
    val isInitialEditing: Boolean = false
)

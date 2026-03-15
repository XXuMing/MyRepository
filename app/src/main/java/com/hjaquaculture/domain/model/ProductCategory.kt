package com.hjaquaculture.domain.model

/**
 * 商品分类领域模型
 */
data class ProductCategory(
    val id: Long,
    val name: String,
    val sort: Int
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 判断是否为系统默认分类或未分类
     * 逻辑：通常约定 id 为 0 或 1 的为基础分类（视业务而定）
     */
    val isDefault: Boolean
        get() = id <= 0L

    /**
     * 获取带有排序编号的名称展示
     * 逻辑：用于下拉列表等场景，方便识别排序权重
     */
    val displayNameWithSort: String
        get() = "[$sort] $name"
}

/**
 * 分类与商品关联模型（领域层复合对象）
 */
data class CategoryWithProducts(
    val category: ProductCategory,
    val products: List<Product>
) {
    /**
     * 计算该分类下商品的总数
     */
    val productCount: Int
        get() = products.size

    /**
     * 计算该分类下所有上架商品的平均价格
     */
    val averagePrice: Long
        get() = if (products.isEmpty()) 0L
        else products.filter { it.isAvailable }.map { it.currentPrice }.average().toLong()
}
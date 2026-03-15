package com.hjaquaculture.domain.model

/**
 * 商品领域模型
 */
data class Product(
    val id: Long,
    val name: String,
    val currentPrice: Long, // 明确标注单位为“分”
    val categoryId: Long,
    val isAvailable: Boolean,
    val sort: Int
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 格式化价格展示 (例如: 99.00)
     * 逻辑解释：将分单位转换为元，并保留两位小数。
     */
    val formattedPrice: String
        get() = "%.2f".format(currentPrice / 100.0)

    /**
     * 获取元单位的 Double 价格
     * 用于 UI 输入框或初步计算
     */
    val priceInYuan: Double
        get() = currentPrice / 100.0

    /**
     * 商品状态标签
     */
    val statusText: String
        get() = if (isAvailable) "发售中" else "已下架"

    /**
     * 业务校验：判断商品是否可以加入购物车
     */
    fun canBeAddedToCart(): Boolean = isAvailable && currentPrice > 0
}
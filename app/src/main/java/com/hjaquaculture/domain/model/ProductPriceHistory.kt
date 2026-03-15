package com.hjaquaculture.domain.model

import kotlin.math.abs

/**
 * 商品调价历史领域模型
 */
data class PriceHistory(
    val id: Long,
    val productId: Long,
    val userId: Long,
    val originalPrice: Long,
    val originalPriceDate: String,
    val newPrice: Long,
    val newPriceDate: String
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 计算价格变化绝对值（分）
     * 逻辑：正数表示涨价，负数表示降价
     */
    val priceDelta: Long
        get() = newPrice - originalPrice

    /**
     * 计算涨跌幅百分比
     * 逻辑：保留两位小数的百分比字符串，如 "+10.5%" 或 "-5.0%"
     */
    val changeRate: String
        get() {
            if (originalPrice == 0L) return "N/A"
            val rate = (priceDelta.toDouble() / originalPrice) * 100
            val prefix = if (rate > 0) "+" else ""
            return "$prefix%.2f%%".format(rate)
        }

    /**
     * 格式化展示价格变动
     * 示例："10.00 -> 12.50"
     */
    val transitionDisplay: String
        get() = "${formatPrice(originalPrice)} -> ${formatPrice(newPrice)}"

    private fun formatPrice(price: Long): String = "%.2f".format(price / 100.0)

    /**
     * 判断是否为大幅度调价（例如超过 50%）
     */
    fun isMajorChange(threshold: Double = 0.5): Boolean {
        if (originalPrice == 0L) return true
        return abs(priceDelta.toDouble() / originalPrice) > threshold
    }
}
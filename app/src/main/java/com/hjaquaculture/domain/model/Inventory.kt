package com.hjaquaculture.domain.model

/**
 * 实时库存领域模型
 */
data class Inventory(
    val productId: Long,
    val quantity: Int,
    val minStock: Int
) {
    // 业务逻辑：是否库存不足
    val isLowStock: Boolean get() = quantity <= minStock

    // 业务逻辑：获取状态颜色（用于 Compose UI）
    val stockStatus: String get() = when {
        quantity <= 0 -> "缺货"
        isLowStock -> "库存预警"
        else -> "充足"
    }
}

/**
 * 变动类型枚举
 */
enum class StockChangeType(val value: Int, val label: String) {
    PURCHASE_IN(1, "采购入库"),
    SALE_OUT(2, "销售出库"),
    ADJUST(3, "盘点调整");

    companion object {
        fun fromInt(v: Int) = entries.find { it.value == v } ?: ADJUST
    }
}

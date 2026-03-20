package com.hjaquaculture.domain.model

import com.hjaquaculture.data.local.entity.InventoryEntity


data class Inventory(
    val productId: Long,
    val amount: Int,
    val minStock: Int,
    val lastUpdatedAt: Long
) {
    // 业务逻辑：是否触发库存预警
    val isLowStock: Boolean get() = amount <= minStock

    // 业务逻辑：缺货缺口
    val shortageQuantity: Int get() = if (isLowStock) minStock - amount else 0

    // 业务逻辑：获取状态颜色（用于 Compose UI）
    val stockStatus: String get() = when {
        amount <= 0 -> "缺货"
        isLowStock -> "库存预警"
        else -> "充足"
    }
}

// Mapper 转换
fun InventoryEntity.toDomain() = Inventory(
    productId = this.productId,
    amount = this.amount,
    minStock = this.minStock,
    lastUpdatedAt = this.lastUpdatedAt
)
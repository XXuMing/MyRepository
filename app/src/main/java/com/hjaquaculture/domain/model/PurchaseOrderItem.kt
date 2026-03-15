package com.hjaquaculture.domain.model

import java.math.BigDecimal

/**
 * 采购订单明细领域模型
 */
data class PurchaseOrderItem(
    val id: Long,
    val orderId: Long,
    val productId: Long,
    val productName: String, // 采购快照名
    val quantity: Int,
    val quantityUnitId: Int,
    val weight: Int,
    val weightUnitId: Int,
    val unitPrice: Long,      // 进货单价（分）
    val subtotal: Long,       // 小计（分）
    val createdAt: Long
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 单价展示
     */
    val unitPriceDisplay: String
        get() = BigDecimal.valueOf(unitPrice).divide(BigDecimal(100)).toString()

    /**
     * 小计展示
     */
    val subtotalDisplay: String
        get() = BigDecimal.valueOf(subtotal).divide(BigDecimal(100)).toString()

    /**
     * 自动计算小计（用于创建订单时的前端预校验）
     * 逻辑：单价 * 重量
     */
    fun calculateExpectedSubtotal(): Long = unitPrice * weight

    /**
     * 验证金额逻辑
     */
    val isAmountValid: Boolean
        get() = subtotal == calculateExpectedSubtotal()
}
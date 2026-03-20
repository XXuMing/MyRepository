package com.hjaquaculture.domain.model

import com.hjaquaculture.common.base.StockUnit
import java.math.BigDecimal

/**
 * 销售订单明细领域模型
 */
data class SaleOrderItem(
    val id: Long,
    val orderId: Long,
    val productId: Long,
    val productName: String, // 快照：记录下单时的商品名称
    val quantity: Int,
    val quantityUnit: StockUnit,
    val weight: Int,
    val weightUnit: StockUnit,
    val unitPrice: Long,      // 存储：分
    val subtotal: Long,       // 存储：分
    val createdAt: Long
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 单价的 BigDecimal 表达
     */
    val unitPriceDecimal: BigDecimal
        get() = BigDecimal.valueOf(unitPrice).divide(BigDecimal(100))

    /**
     * 小计的 BigDecimal 表达
     */
    val subtotalDecimal: BigDecimal
        get() = BigDecimal.valueOf(subtotal).divide(BigDecimal(100))

    /**
     * 校验逻辑：验证小计是否正确
     * 逻辑：小计 = 数量 * 单价
     * 注意：如果业务涉及重量计价，此处公式需根据实际情况调整
     */
    fun isSubtotalCorrect(): Boolean {
        val calculated = unitPrice * quantity
        return calculated == subtotal
    }

    /**
     * 获取重量描述
     * 示例："10斤"
     */
    fun getWeightLabel(unitName: String): String {
        return "$weight $unitName"
    }
}
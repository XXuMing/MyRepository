package com.hjaquaculture.domain.model

import com.hjaquaculture.common.utils.InvoiceStatus
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 销售发票领域模型
 */
data class SaleInvoice(
    val id: Long,
    val sn: String,
    val customerId: Long,
    val customerName: String, // 快照名称
    val creatorId: Long,
    val creatorName: String,  // 快照名称
    val amountDue: Long,      // 应付 (Snapshot from Order)
    val amountPaid: Long,     // 实付
    val amountRem: Long,      // 欠款
    val status: InvoiceStatus,
    val remark: String,
    val createdAt: Long,
    val isDeleted: Boolean
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 支付进度百分比 (0.00 - 1.00)
     */
    val paymentProgress: Float
        get() = if (amountDue == 0L) 1f else amountPaid.toFloat() / amountDue

    /**
     * 获取金额展示（BigDecimal 转换）
     */
    val amountDueDisplay: String get() = formatLongPrice(amountDue)
    val amountPaidDisplay: String get() = formatLongPrice(amountPaid)
    val amountRemDisplay: String get() = formatLongPrice(amountRem)

    /**
     * 核心校验：发票数据是否平衡
     * 逻辑：应付金额 必须等于 实付金额 + 剩余金额
     */
    val isBalanced: Boolean
        get() = amountDue == (amountPaid + amountRem)

    /**
     * 业务逻辑：判断是否允许继续支付
     * 逻辑：未作废且还有欠款
     */
    val canProcessPayment: Boolean
        get() = status != InvoiceStatus.PARTIALLY_PAID && amountRem > 0

    private fun formatLongPrice(price: Long): String {
        return BigDecimal.valueOf(price)
            .divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
            .toString()
    }
}
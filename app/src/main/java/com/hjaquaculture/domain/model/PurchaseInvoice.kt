package com.hjaquaculture.domain.model

import com.hjaquaculture.common.utils.InvoiceStatus
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 采购账单领域模型
 */
data class PurchaseInvoice(
    val id: Long,
    val sn: String,
    val supplierId: Long,
    val supplierName: String, // 供应商快照名
    val creatorId: Long,
    val creatorName: String,  // 经手人快照名
    val amountPayable: Long,  // 应付总额（分）
    val amountPaid: Long,     // 实付金额（分）
    val amountRem: Long,      // 剩余欠款（分）
    val status: InvoiceStatus,
    val remark: String,
    val createdAt: Long,
    val isDeleted: Boolean
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 财务平衡校验
     * 逻辑：应付金额 = 已付 + 欠款
     */
    val isFinancialBalanced: Boolean
        get() = amountPayable == (amountPaid + amountRem)

    /**
     * 获取支付比例 (0.00 - 1.00)
     */
    val paymentRatio: Float
        get() = if (amountPayable == 0L) 1f else amountPaid.toFloat() / amountPayable

    /**
     * 金额展示格式化
     */
    val amountPayableDisplay: String get() = formatPrice(amountPayable)
    val amountPaidDisplay: String get() = formatPrice(amountPaid)
    val amountRemDisplay: String get() = formatPrice(amountRem)

    /**
     * 业务逻辑：判断是否处于欠款状态
     */
    val hasOutstandingBalance: Boolean
        get() = status != InvoiceStatus.PARTIALLY_PAID && amountRem > 0

    private fun formatPrice(price: Long): String {
        return BigDecimal.valueOf(price)
            .divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
            .toString()
    }
}
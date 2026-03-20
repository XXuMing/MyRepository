package com.hjaquaculture.domain.model

import com.hjaquaculture.common.base.InvoiceStatus
import com.hjaquaculture.common.base.InvoiceSymbol

/**
 * 财务发票概要领域模型
 */
data class CombinedInvoice(
    val symbol: InvoiceSymbol,
    val id: Long,
    val sn: String,
    val partnerId: Long,
    val partnerName: String,
    val creatorId: Long,
    val creatorName: String,
    val amountTotal: Long,
    val amountPaid: Long,
    val amountRem: Long,
    val status: InvoiceStatus,
    val remark: String,
    val createdAt: Long
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 判断是否为收入性质
     */
    val isRevenue: Boolean get() = symbol == InvoiceSymbol.SALE

    /**
     * 判断是否为支出性质
     */
    val isExpense: Boolean get() = symbol == InvoiceSymbol.PURCHASE

    /**
     * 格式化总金额显示
     */
    val amountTotalDisplay: String
        get() = "%.2f".format(amountTotal / 100.0)

    /**
     * 支付进度百分比 (0-100)
     */
    val paymentProgress: Int
        get() = if (amountTotal == 0L) 100 else ((amountPaid.toDouble() / amountTotal) * 100).toInt()
}


/**
 * 账单详情的封装容器
 */
sealed class InvoicePaymentsData {
    data class Sale(val data: List<SalePayment>) : InvoicePaymentsData()
    data class Purchase(val data: List<PurchasePayment>) : InvoicePaymentsData()
}
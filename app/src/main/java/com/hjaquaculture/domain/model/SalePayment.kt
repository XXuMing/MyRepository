package com.hjaquaculture.domain.model

import com.hjaquaculture.common.utils.PaymentMethods
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 销售流水领域模型
 */
data class SalePayment(
    val id: Long,
    val sn: String,
    val invoiceId: Long,
    val customerId: Long,
    val amount: Long, // 统一使用 Long 存储（分）
    val paymentTime: Long,
    val paymentMethods: PaymentMethods
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 金额展示
     */
    val amountDisplay: String
        get() = "%.2f".format(amount / 100.0)

    /**
     * 友好的时间展示
     */
    val formattedTime: String
        get() = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(paymentTime),
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

    /**
     * 业务规则：判断是否为大额支付
     * 示例：超过 10,000 元标记为大额
     */
    fun isLargeAmount(): Boolean = amount >= 10000_00L
}
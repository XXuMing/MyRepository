package com.hjaquaculture.domain.model

import com.hjaquaculture.common.utils.PaymentMethods
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 采购流水领域模型
 */
data class PurchasePayment(
    val id: Long,
    val sn: String,
    val invoiceId: Long,
    val supplierId: Long,
    val amount: Long,           // 存储：分
    val paymentTime: Long,
    val paymentMethods: PaymentMethods
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 金额显示 (BigDecimal 转换)
     */
    val amountDisplay: String
        get() = BigDecimal.valueOf(amount)
            .divide(BigDecimal(100), 2, java.math.RoundingMode.HALF_UP)
            .toString()

    /**
     * 付款时间格式化
     */
    val formattedPaymentTime: String
        get() = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(paymentTime),
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

    /**
     * 支付详情摘要
     */
    val paymentSummary: String
        get() = " ${paymentMethods.description} 支付了 $amountDisplay"
}

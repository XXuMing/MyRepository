package com.hjaquaculture.feature.invoice

import androidx.compose.runtime.Immutable
import com.hjaquaculture.common.base.TradeSymbol
import com.hjaquaculture.common.utils.TimeUtils.toFormattedString
import com.hjaquaculture.domain.model.CombinedInvoice
import com.hjaquaculture.domain.model.PurchasePayment
import com.hjaquaculture.domain.model.SalePayment

/**
 * 发票概要 视图对象
 * @param symbol 标识
 * @param symbolDescription 标识描述
 * @param syntheticId 合成ID
 * @param originalId 原始ID
 * @param sn 单号
 * @param partnerId 合作伙伴ID
 * @param partnerName 合作伙伴名称
 * @param creatorId 创建者ID
 * @param creatorName 创建者名称
 * @param amountTotal 总金额
 * @param amountPaid 已付金额
 * @param amountRem 欠款金额
 * @param status 状态
 * @param remark 备注
 * @param createdAt 创建时间
 * @param isDetailsExpanded 是否展开详情
 */
@Immutable
data class InvoiceVO(
    val symbol: TradeSymbol,
    val symbolDescription: String,
    val syntheticId: String,
    val originalId:Long,
    val sn: String?,

    val creatorId: Long,
    val creatorName: String,
    val partnerId: Long,
    val partnerName: String,
    val amountTotal: String,
    val amountPaid: String,
    val amountRem: String,

    val status: String,
    val remark: String?,
    val createdAt: String,

    val isDetailsExpanded: Boolean = false,
)

fun CombinedInvoice.toVO(): InvoiceVO{
    return InvoiceVO(
        symbol = symbol,
        symbolDescription = symbol.description,
        syntheticId = "${symbol.name}_$id",
        originalId = id,
        sn = sn,

        creatorId = creatorId,
        creatorName = creatorName,
        partnerId = partnerId,
        partnerName = partnerName,

        amountTotal = "¥${amountTotal / 100}",
        amountPaid = "¥${amountPaid / 100}",
        amountRem = "¥${amountRem / 100}",
        status = status.description,

        createdAt = createdAt.toFormattedString("yyyy-MM-dd"),
        remark = remark,
        isDetailsExpanded = false
    )
}

// ---- 流水 VO ----
@Immutable
data class PaymentItemVO(
    val id: Long,
    val sn: String,
    val amount: String,       // 已格式化（元）
    val paymentTime: String,  // 已格式化（yyyy-MM-dd HH:mm）
    val paymentMethod: String // 支付方式描述
)

fun SalePayment.toVO(): PaymentItemVO = PaymentItemVO(
    id = id,
    sn = sn,
    amount = "¥${"%.2f".format(amount / 100.0)}",
    paymentTime = paymentTime.toFormattedString("yyyy-MM-dd HH:mm"),
    paymentMethod = paymentMethods.description
)

fun PurchasePayment.toVO(): PaymentItemVO = PaymentItemVO(
    id = id,
    sn = sn,
    amount = "¥${"%.2f".format(amount / 100.0)}",
    paymentTime = paymentTime.toFormattedString("yyyy-MM-dd HH:mm"),
    paymentMethod = paymentMethods.description
)
package com.hjaquaculture.feature.invoice

import androidx.compose.runtime.Immutable
import com.hjaquaculture.common.utils.InvoiceSymbol
import com.hjaquaculture.common.utils.TimeUtils.toFormattedString
import com.hjaquaculture.domain.model.CombinedInvoice

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
    val symbol: InvoiceSymbol,
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
        syntheticId = "${symbol.dbValue}_$id",
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

/**
 * 采购明细概要 视图对象
 * @param id 主键
 * @param orderId 采购订单ID
 * @param productId 商品ID
 * @param quantity 数量
 * @param unit 单位（件、箱、袋）
 * @param weight 重量（斤）
 * @param unitPrice 单价（元）
 * @param subtotal 小计（元）
 */
@Immutable
data class PaymentItemVO(
    val id : Long,
    val orderId: Long,
    val productId: Long,

    val productName: String,
    val quantity: String,
    val unit: String,
    val weight: String,
    val unitPrice: String,
    val subtotal:String
)
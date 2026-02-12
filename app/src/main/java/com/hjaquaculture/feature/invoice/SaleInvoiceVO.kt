package com.hjaquaculture.feature.invoice

import androidx.compose.runtime.Immutable
import com.hjaquaculture.common.utils.TimeUtils.toFormattedString
import com.hjaquaculture.data.local.entity.SaleInvoice

/**
 * 销售发票概要 视图对象
 * @param id 主键
 * @param sn 单号
 * @param saleOrderId 销售订单ID
 * @param customerName 客户名称
 * @param creatorName 创建者
 * @param amountDue 应付金额
 * @param amountPaid 实付金额
 * @param amountRem 欠款金额
 * @param status 状态
 * @param createdAt 创建时间
 * @param remark 备注
 * @param isOrderSummaryExpanded 是否展开订单概要
 * @param isPaymentSummaryExpanded 是否展开支付概要
 */
@Immutable
data class SaleInvoiceSummaryVO(
    val id: Long,
    val sn: String,
    val saleOrderId: Long,

    val customerName: String,
    val creatorName: String,
    val amountDue: String,
    val amountPaid: String,
    val amountRem: String,
    val status: String,
    val createdAt: String,
    val remark: String,

    val isOrderSummaryExpanded: Boolean = false,
    val isPaymentSummaryExpanded: Boolean = false,
)

fun SaleInvoice.toVO(): SaleInvoiceSummaryVO{
    return SaleInvoiceSummaryVO(
        id = id,
        sn = sn?:"无单号",
        saleOrderId = orderId,

        customerName = customerName,
        creatorName = userName,
        amountDue = "¥${amountDue}",
        amountPaid = "¥${amountPaid}",
        amountRem = "¥${amountRem}",
        status = status.description,
        createdAt = createdAt.toFormattedString("yyyy-MM-dd"),
        remark = remark?:"none",
        isOrderSummaryExpanded = false,
        isPaymentSummaryExpanded = false,
    )
}

/**
 * 销售订单概要 视图对象
 * @param id 主键
 * @param sn 单号
 * @param userName 操作者
 * @param customerName 客户名称
 * @param orderType 订单类型
 * @param orderStatus 订单状态
 * @param remark 备注
 * @param createdAt 创建时间
 * @param timeLimit 限定日期
 */
@Immutable
data class SaleOrderSummaryVO(
    val id: Long,
    val sn: String?,
    val userName: String,
    val customerName: String,
    val orderType: String,
    val orderStatus: String,
    val remark: String,
    val createdAt: String,
    val timeLimit: String,
)

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
data class SalePaymentSummaryVO(
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
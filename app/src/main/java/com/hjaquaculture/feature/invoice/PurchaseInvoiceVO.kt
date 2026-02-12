package com.hjaquaculture.feature.invoice

import androidx.compose.runtime.Immutable
import com.hjaquaculture.common.utils.TimeUtils.toFormattedString
import com.hjaquaculture.data.local.entity.PurchaseInvoice

/**
 * 销售发票概要 视图对象
 * @param id 主键
 * @param sn 单号
 * @param purchaseOrderId 采购订单ID
 * @param supplierName 供应商名称
 * @param creatorName 创建者
 * @param amountPayable 应付金额
 * @param amountPaid 实付金额
 * @param amountRem 欠款金额
 * @param status 状态
 * @param createdAt 创建时间
 * @param remark 备注
 * @param isOrderSummaryExpanded 是否展开订单概要
 * @param isPaymentSummaryExpanded 是否展开支付概要
 */
@Immutable
data class PurchaseInvoiceSummaryVO(
    val id: Long,
    val sn: String,
    val purchaseOrderId: Long,

    val supplierName: String,
    val creatorName: String,
    val amountPayable: String,
    val amountPaid: String,
    val amountRem: String,
    val status: String,
    val createdAt: String,
    val remark: String,

    val isOrderSummaryExpanded: Boolean = false,
    val isPaymentSummaryExpanded: Boolean = false,
)

fun PurchaseInvoice.toVO(): PurchaseInvoiceSummaryVO{
    return PurchaseInvoiceSummaryVO(
        id = id,
        sn = sn?:"无单号",
        purchaseOrderId = orderId,
        supplierName = supplierName,
        creatorName = userName,
        amountPayable = "¥${amountPayable}",
        amountPaid = "¥${amountPaid}",
        amountRem = "¥${amountRem}",
        status = this.status.description,
        createdAt = createdAt.toFormattedString("yyyy-MM-dd"),
        remark = remark?:"none",
        isOrderSummaryExpanded = false,
        isPaymentSummaryExpanded = false,
    )
}
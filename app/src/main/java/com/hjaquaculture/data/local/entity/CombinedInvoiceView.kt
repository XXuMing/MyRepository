package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.hjaquaculture.common.base.InvoiceStatus
import com.hjaquaculture.common.base.InvoiceSymbol

/**
 * 发票概要 视图对象
 * @param symbol 标识
 * @param id 主键
 * @param sn 单号
 * @param partnerId 合作伙伴ID
 * @param partnerName 合作伙伴名称
 * @param creatorId 操作者ID
 * @param creatorName 操作者名称
 * @param amountTotal 总金额
 * @param amountPaid 已付金额
 * @param amountRem 欠款金额
 * @param status 状态
 * @param remark 备注
 * @param createdAt 创建时间
 */
@DatabaseView(
    viewName = "combined_invoice_view",
    value = """
SELECT 'SALE_INVOICE' AS symbol,
    id , sn ,
    customer_id AS partner_id , customer_name AS partner_name ,
    creator_id , creator_name ,
    amount_due AS amount_total ,
    amount_paid , amount_rem ,
    status , remark , created_at , is_deleted
FROM sale_invoice
UNION ALL
SELECT 'PUR_INVOICE' AS symbol ,
    id , sn ,
    supplier_id AS partner_id , supplier_name AS partner_name ,
    creator_id , creator_name , 
    amount_payable AS amount_total ,
    amount_paid , amount_rem ,
    status , remark , created_at , is_deleted
FROM purchase_invoice   
"""
)
data class CombinedInvoiceView(
    @ColumnInfo("symbol")
    val symbol: InvoiceSymbol,

    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("sn")
    val sn: String?,

    @ColumnInfo("partner_id")
    val partnerId: Long,

    @ColumnInfo("partner_name")
    val partnerName: String,

    @ColumnInfo("creator_id")
    val creatorId: Long,

    @ColumnInfo("creator_name")
    val creatorName: String,

    @ColumnInfo("amount_total")
    val amountTotal: Long,

    @ColumnInfo("amount_paid")
    val amountPaid: Long,

    @ColumnInfo("amount_rem")
    val amountRem: Long,

    @ColumnInfo("status")
    val status: InvoiceStatus,

    @ColumnInfo("remark")
    val remark: String?,

    @ColumnInfo("created_at")
    val createdAt: Long,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
)
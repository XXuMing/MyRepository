package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.hjaquaculture.common.base.DeliveryMethod
import com.hjaquaculture.common.base.OrderStatus
import com.hjaquaculture.common.base.OrderSymbol

/**
 * 合并订单视图
 * @param symbol 标识
 * @param id 主键
 * @param sn 单号
 * @param creatorId 操作者ID
 * @param creatorName 操作者名称
 * @param partnerId 合作伙伴ID
 * @param partnerName 合作伙伴名称
 * @param deliveryMethod 订单类型
 * @param orderStatus 订单状态
 * @param totalPrice 总金额
 * @param totalQuantity 总数量
 * @param remark 备注
 * @param createdAt 创建时间
 * @param expiredAt 预定处理日期
 */

@DatabaseView(
    viewName = "combined_order_view",
    value = """
SELECT 'SALE_ORDER' AS symbol,
    s.id , s.sn , s.creator_id , s.invoice_id , s.invoice_sn ,
    u.name AS creator_name ,
    s.customer_id AS partner_id ,
    c.name AS partner_name ,
    s.order_type , s.order_status , s.total_price , s.total_quantity , 
    s.remark , s.created_at , s.expired_at , s.is_deleted
FROM sale_order s
LEFT JOIN user u ON s.creator_id = u.id
LEFT JOIN customer c ON s.customer_id = c.id

UNION ALL
SELECT 'PUR_ORDER' AS symbol,
    p.id , p.sn , p.creator_id , p.invoice_id , p.invoice_sn ,
    u.name AS user_name ,
    p.supplier_id AS partner_id ,
    sup.name AS partner_name ,
    p.order_type , p.order_status , p.total_price , p.total_quantity , 
    p.remark , p.created_at , p.expired_at , p.is_deleted
FROM purchase_order p
LEFT JOIN user u ON p.creator_id = u.id
LEFT JOIN supplier sup ON p.supplier_id = sup.id
"""
)
data class CombinedOrderView(
    @ColumnInfo("symbol")
    val symbol: OrderSymbol,

    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("sn")
    val sn: String?,

    @ColumnInfo("invoice_id")
    val invoiceId: Long ?= null,

    @ColumnInfo("invoice_sn")
    val invoiceSn: String ?= null,

    @ColumnInfo("creator_id")
    val creatorId: Long,

    @ColumnInfo("creator_name")
    val creatorName: String,

    @ColumnInfo("partner_id")
    val partnerId: Long,

    @ColumnInfo("partner_name")
    val partnerName: String,

    @ColumnInfo("order_type")
    val deliveryMethod: DeliveryMethod,

    @ColumnInfo("order_status")
    val orderStatus: OrderStatus,

    @ColumnInfo("total_price")
    val totalPrice: Long,

    @ColumnInfo("total_quantity")
    val totalQuantity: Int,

    @ColumnInfo("remark")
    val remark: String,

    @ColumnInfo("created_at")
    val createdAt: Long,

    @ColumnInfo("expired_at")
    val expiredAt: Long ?= null,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
)
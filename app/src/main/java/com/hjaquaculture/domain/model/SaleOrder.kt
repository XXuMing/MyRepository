package com.hjaquaculture.domain.model

import com.hjaquaculture.common.utils.OrderStatus
import com.hjaquaculture.common.utils.OrderType

/**
 * 销售订单领域模型
 */
data class SaleOrder(
    val id: Long,
    val sn: String,
    val invoiceId: Long?,
    val invoiceSn: String,
    val creatorId: Long,
    val customerId: Long,
    val orderType: OrderType,
    val orderStatus: OrderStatus,
    val totalPrice: Long, // 存储依然使用 Long (分)
    val totalQuantity: Int,
    val remark: String,
    val expiredAt: Long?,
    val createdAt: Long,
    val isDeleted: Boolean
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 价格展示逻辑
     */
    val totalPriceDisplay: String
        get() = "%.2f".format(totalPrice / 100.0)

    /**
     * 状态机逻辑：判断订单是否可以编辑
     * 逻辑：只有草稿和预定状态允许修改
     */
    val canEdit: Boolean
        get() = orderStatus == OrderStatus.DRAFT || orderStatus == OrderStatus.RESERVATION

    /**
     * 状态机逻辑：判断是否允许取消
     */
    val canCancel: Boolean
        get() = orderStatus != OrderStatus.COMPLETED && orderStatus != OrderStatus.CANCELLED

    /**
     * 预定过期检查
     * 逻辑：如果当前时间超过 expiredAt 且状态仍为 RESERVATION，则判定为逾期
     */
    val isExpired: Boolean
        get() = orderStatus == OrderStatus.RESERVATION &&
                expiredAt != null && System.currentTimeMillis() > expiredAt

    /**
     * 订单摘要
     */
    val summary: String
        get() = "订单 $sn - 共 $totalQuantity 件商品"
}
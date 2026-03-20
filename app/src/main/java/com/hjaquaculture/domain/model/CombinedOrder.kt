package com.hjaquaculture.domain.model

import com.hjaquaculture.common.base.DeliveryMethod
import com.hjaquaculture.common.base.OrderStatus
import com.hjaquaculture.common.base.OrderSymbol

/**
 * 订单概要领域模型（综合销售与采购）
 */
data class CombinedOrder(
    val symbol: OrderSymbol,
    val id: Long,
    val sn: String,
    val partnerId: Long,
    val partnerName: String,
    val creatorId: Long,
    val creatorName: String,
    val deliveryMethod: DeliveryMethod,
    val orderStatus: OrderStatus,
    val totalPrice: Long,
    val totalQuantity: Int,
    val remark: String,
    val createdAt: Long,
    val expiredAt: Long?
) {
    // --- 业务逻辑 (Business Rules) ---

    /**
     * 统一金额格式化
     */
    val totalPriceDisplay: String
        get() = "%.2f".format(totalPrice / 100.0)

    /**
     * 业务类型标识判断
     */
    val isSale: Boolean get() = symbol == OrderSymbol.SALE
    val isPurchase: Boolean get() = symbol == OrderSymbol.PURCHASE

    /**
     * 全局逾期判断
     * 逻辑：如果状态不是“完成”或“作废”，且当前时间超过了处理截止日期
     */
    val isOverdue: Boolean
        get() = orderStatus != OrderStatus.COMPLETED &&
                orderStatus != OrderStatus.CANCELLED &&
                expiredAt != null && System.currentTimeMillis() > expiredAt
}

/**
 * 订单详情的封装容器
 */
sealed class OrderItemsData {
    data class Sale(val data: List<SaleOrderItem>) : OrderItemsData()
    data class Purchase(val data: List<PurchaseOrderItem>) : OrderItemsData()
}
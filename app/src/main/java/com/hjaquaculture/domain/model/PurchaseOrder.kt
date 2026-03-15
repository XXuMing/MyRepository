package com.hjaquaculture.domain.model

import com.hjaquaculture.common.utils.OrderStatus
import com.hjaquaculture.common.utils.OrderType
import java.math.BigDecimal

/**
 * 采购订单领域模型
 */
data class PurchaseOrder(
    val id: Long,
    val sn: String,
    val invoiceId: Long?,
    val invoiceSn: String,
    val supplierId: Long,
    val supplierName: String, // 快照：记录下单时的供应商名称
    val creatorId: Long,
    val orderType: OrderType,
    val orderStatus: OrderStatus,
    val totalPrice: Long,
    val totalQuantity: Int,
    val remark: String,
    val approvedAt: Long?,    // 对应 Entity 的 expiredAt，语义化为“审核时间”
    val createdAt: Long,
    val isDeleted: Boolean
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 价格展示 (BigDecimal 转换)
     */
    val totalPriceDisplay: String
        get() = BigDecimal.valueOf(totalPrice)
            .divide(BigDecimal(100), 2, java.math.RoundingMode.HALF_UP)
            .toString()

    /**
     * 业务状态：是否允许编辑
     * 逻辑：只有在草稿或预定状态下才允许修改采购内容
     */
    val canModify: Boolean
        get() = orderStatus == OrderStatus.DRAFT || orderStatus == OrderStatus.RESERVATION

    /**
     * 业务逻辑：是否已完成审核
     */
    val isApproved: Boolean
        get() = orderStatus != OrderStatus.DRAFT && orderStatus != OrderStatus.RESERVATION && approvedAt != null

    /**
     * 订单标识摘要
     */
    val displayTitle: String
        get() = "采购单: $sn ($supplierName)"
}
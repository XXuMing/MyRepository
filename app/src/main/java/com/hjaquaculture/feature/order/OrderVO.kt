package com.hjaquaculture.feature.order

import androidx.compose.runtime.Immutable
import com.hjaquaculture.common.base.OrderSymbol
import com.hjaquaculture.common.utils.TimeUtils.toFormattedString
import com.hjaquaculture.domain.model.CombinedOrder
import com.hjaquaculture.domain.model.PurchaseOrderItem
import com.hjaquaculture.domain.model.SaleOrderItem


/**
 * 订单概要 视图对象
 * @param symbol 订单分类 标识
 * @param symbolDescription 标识描述
 * @param syntheticId 合成ID
 * @param originalId 原始ID
 * @param sn 单号
 * @param creatorId 操作者ID
 * @param creatorName 操作者名称
 * @param partnerId 合作伙伴ID
 * @param partnerName 合作伙伴名称
 * @param orderType 订单类型
 * @param orderStatus 订单状态
 * @param totalPrice 总金额
 * @param totalQuantity 总数量
 * @param remark 备注
 * @param createdAt 创建时间
 * @param expiredAt 预定处理日期
 */
@Immutable
data class OrderVO(
    val symbol: OrderSymbol,
    val symbolDescription: String,
    val syntheticId: String,
    val originalId:Long,
    val sn: String?,

    val creatorId: Long,
    val creatorName: String,
    val partnerId: Long,
    val partnerName: String,

    val orderType: String,
    val orderStatus: String,

    val totalPrice: String,
    val totalQuantity: String,
    val remark: String,

    val createdAt: String,
    val expiredAt: String,
)

/**
 * 将 CombinedOrderView 转换为 OrderVO
 */
fun CombinedOrder.toVO(): OrderVO{
    return OrderVO(
        symbol = symbol,
        symbolDescription = symbol.description,
        syntheticId = "${symbol.dbValue}_$id",
        originalId = id,
        sn = sn,

        creatorId = creatorId,
        creatorName = creatorName,
        partnerId = partnerId,
        partnerName = partnerName,

        totalPrice = "¥${totalPrice / 100}",
        totalQuantity = totalQuantity.toString(),
        orderType = deliveryMethod.description,
        orderStatus = orderStatus.description,

        remark = remark,
        createdAt = createdAt.toFormattedString("yyyy-MM-dd"),
        expiredAt = expiredAt?.toFormattedString("yyyy-MM-dd") ?: "空"
    )
}

/**
 * 订单明细 视图对象
 */
data class OrderItemVO(
    val symbol: OrderSymbol,
    val id: Long,
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val quantityUnitId: Int,
    val weight: Int,
    val weightUnitId: Int,
    val unitPrice: Long,
    val subtotal: Long,
    val createdAt: Long
)

/**
 * 将 SaleOrderItemEntity 转换为 OrderItemVO
 */
fun SaleOrderItem.toVO(symbol: OrderSymbol): OrderItemVO {
    return OrderItemVO(
        symbol = symbol,
        id = id,
        orderId = orderId,
        productId = productId,
        productName = productName,
        quantity = quantity,
        quantityUnitId = quantityUnitId,
        weight = weight,
        weightUnitId = weightUnitId,
        unitPrice = unitPrice,
        subtotal = subtotal,
        createdAt = createdAt
    )
}

/**
 * 将 PurchaseOrderItemEntity 转换为 OrderItemVO
 */
fun PurchaseOrderItem.toVO(symbol: OrderSymbol): OrderItemVO {
    return OrderItemVO(
        symbol = symbol,
        id = id,
        orderId = orderId,
        productId = productId,
        productName = productName,
        quantity = quantity,
        quantityUnitId = quantityUnitId,
        weight = weight,
        weightUnitId = weightUnitId,
        unitPrice = unitPrice,
        subtotal = subtotal,
        createdAt = createdAt
    )
}




enum class OrderSymbolFilter(val symbol: String?, val label: String) {
    ALL(null, "全部订单"), // null 表示不限制前缀
    SALE("SALE_ORDER", "销售订单"),
    PURCHASE("PUR_ORDER", "采购订单");
}

enum class OrderTypeFilter(val type: Int?, val label: String){
    ALL(null,"全部类型"),
    PICKUP(1, "自提"),
    FREIGHT(2, "货运");
}

enum class OrderStatusFilter(val status:Int?,val label: String){
    All(null,"全部状态"),
    CANCELLED(-1,"作废"),
    DRAFT(0,"草稿"),
    PENDING(10,"预定"),
    CONFIRMED(20,"确认"),
    COMPLETED(30,"完成");
}
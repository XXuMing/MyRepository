package com.hjaquaculture.data.local.mapper

import com.hjaquaculture.data.local.entity.CombinedOrderView
import com.hjaquaculture.data.local.entity.PurchaseOrderEntity
import com.hjaquaculture.data.local.entity.PurchaseOrderItemEntity
import com.hjaquaculture.data.local.entity.SaleOrderEntity
import com.hjaquaculture.data.local.entity.SaleOrderItemEntity
import com.hjaquaculture.domain.model.CombinedOrder
import com.hjaquaculture.domain.model.PurchaseOrder
import com.hjaquaculture.domain.model.PurchaseOrderItem
import com.hjaquaculture.domain.model.SaleOrder
import com.hjaquaculture.domain.model.SaleOrderItem

/**
 * SaleOrderMapper
 */

fun SaleOrderEntity.toDomain(): SaleOrder {
    return SaleOrder(
        id = this.id,
        sn = this.sn ?: "N/A",
        invoiceId = this.invoiceId,
        invoiceSn = this.invoiceSn ?: "",
        creatorId = this.creatorId,
        customerId = this.customerId,
        orderType = this.orderType,
        orderStatus = this.orderStatus,
        totalPrice = this.totalPrice,
        totalQuantity = this.totalQuantity,
        remark = this.remark ?: "",
        expiredAt = this.expiredAt,
        createdAt = this.createdAt,
        isDeleted = this.isDeleted
    )
}

fun SaleOrder.toEntity(): SaleOrderEntity {
    return SaleOrderEntity(
        id = this.id,
        sn = this.sn,
        invoiceId = this.invoiceId,
        invoiceSn = this.invoiceSn,
        creatorId = this.creatorId,
        customerId = this.customerId,
        orderType = this.orderType,
        orderStatus = this.orderStatus,
        totalPrice = this.totalPrice,
        totalQuantity = this.totalQuantity,
        remark = this.remark,
        expiredAt = this.expiredAt,
        createdAt = this.createdAt,
        isDeleted = this.isDeleted
    )
}

/**
 * SaleOrderItemMapper
 */

// --- Entity -> Domain ---
fun SaleOrderItemEntity.toDomain(): SaleOrderItem {
    return SaleOrderItem(
        id = this.id,
        orderId = this.orderId,
        productId = this.productId,
        productName = this.productName,
        quantity = this.quantity,
        quantityUnitId = this.quantityUnitId,
        weight = this.weight,
        weightUnitId = this.weightUnitId,
        unitPrice = this.unitPrice,
        subtotal = this.subtotal,
        createdAt = this.createdAt
    )
}

// --- Domain -> Entity ---
fun SaleOrderItem.toEntity(): SaleOrderItemEntity {
    return SaleOrderItemEntity(
        id = this.id,
        orderId = this.orderId,
        productId = this.productId,
        productName = this.productName,
        quantity = this.quantity,
        quantityUnitId = this.quantityUnitId,
        weight = this.weight,
        weightUnitId = this.weightUnitId,
        unitPrice = this.unitPrice,
        subtotal = this.subtotal,
        createdAt = this.createdAt
    )
}

/**
 * PurchaseOrderMapper
 */

fun PurchaseOrderEntity.toDomain(): PurchaseOrder {
    return PurchaseOrder(
        id = this.id,
        sn = this.sn ?: "P-N/A",
        invoiceId = this.invoiceId,
        invoiceSn = this.invoiceSn ?: "",
        supplierId = this.supplierId,
        supplierName = this.supplierName,
        creatorId = this.creatorId,
        orderType = this.orderType,
        orderStatus = this.orderStatus,
        totalPrice = this.totalPrice,
        totalQuantity = this.totalQuantity,
        remark = this.remark ?: "",
        approvedAt = this.expiredAt, // 映射到 approvedAt
        createdAt = this.createdAt,
        isDeleted = this.isDeleted
    )
}

fun PurchaseOrder.toEntity(): PurchaseOrderEntity {
    return PurchaseOrderEntity(
        id = this.id,
        sn = this.sn,
        invoiceId = this.invoiceId,
        invoiceSn = this.invoiceSn,
        supplierId = this.supplierId,
        supplierName = this.supplierName,
        creatorId = this.creatorId,
        orderType = this.orderType,
        orderStatus = this.orderStatus,
        totalPrice = this.totalPrice,
        totalQuantity = this.totalQuantity,
        remark = this.remark,
        expiredAt = this.approvedAt,
        createdAt = this.createdAt,
        isDeleted = this.isDeleted
    )
}

/**
 * PurchaseOrderItemMapper
 */

fun PurchaseOrderItemEntity.toDomain(): PurchaseOrderItem {
    return PurchaseOrderItem(
        id = this.id,
        orderId = this.orderId,
        productId = this.productId,
        productName = this.productName,
        quantity = this.quantity,
        quantityUnitId = this.quantityUnitId,
        weight = this.weight,
        weightUnitId = this.weightUnitId,
        unitPrice = this.unitPrice,
        subtotal = this.subtotal,
        createdAt = this.createdAt
    )
}

fun PurchaseOrderItem.toEntity(): PurchaseOrderItemEntity {
    return PurchaseOrderItemEntity(
        id = this.id,
        orderId = this.orderId,
        productId = this.productId,
        productName = this.productName,
        quantity = this.quantity,
        quantityUnitId = this.quantityUnitId,
        weight = this.weight,
        weightUnitId = this.weightUnitId,
        unitPrice = this.unitPrice,
        subtotal = this.subtotal,
        createdAt = this.createdAt
    )
}

/**
 * CombinedOrderMapper
 */

fun CombinedOrderView.toDomain(): CombinedOrder {
    return CombinedOrder(
        symbol = this.symbol,
        id = this.id,
        sn = this.sn ?: "NO-SN",
        partnerId = this.partnerId,
        partnerName = this.partnerName,
        creatorId = this.creatorId,
        creatorName = this.creatorName,
        orderType = this.orderType,
        orderStatus = this.orderStatus,
        totalPrice = this.totalPrice,
        totalQuantity = this.totalQuantity,
        remark = this.remark,
        createdAt = this.createdAt,
        expiredAt = this.expiredAt
    )
}
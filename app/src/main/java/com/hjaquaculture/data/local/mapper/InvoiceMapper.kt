package com.hjaquaculture.data.local.mapper

import com.hjaquaculture.data.local.entity.CombinedInvoiceView
import com.hjaquaculture.data.local.entity.PurchaseInvoiceEntity
import com.hjaquaculture.data.local.entity.SaleInvoiceEntity
import com.hjaquaculture.data.local.entity.SalePaymentEntity
import com.hjaquaculture.domain.model.CombinedInvoice
import com.hjaquaculture.domain.model.PurchaseInvoice
import com.hjaquaculture.domain.model.SaleInvoice
import com.hjaquaculture.domain.model.SalePayment

/**
 * SaleInvoiceMapper
 */

fun SaleInvoiceEntity.toDomain(): SaleInvoice {
    return SaleInvoice(
        id = this.id,
        sn = this.sn ?: "INV-N/A",
        customerId = this.customerId,
        customerName = this.customerName,
        creatorId = this.creatorId,
        creatorName = this.creatorName,
        amountDue = this.amountDue,
        amountPaid = this.amountPaid,
        amountRem = this.amountRem,
        status = this.status,
        remark = this.remark ?: "",
        createdAt = this.createdAt,
        isDeleted = this.isDeleted
    )
}

fun SaleInvoice.toEntity(): SaleInvoiceEntity {
    return SaleInvoiceEntity(
        id = this.id,
        sn = this.sn,
        customerId = this.customerId,
        customerName = this.customerName,
        creatorId = this.creatorId,
        creatorName = this.creatorName,
        amountDue = this.amountDue,
        amountPaid = this.amountPaid,
        amountRem = this.amountRem,
        status = this.status,
        remark = this.remark,
        createdAt = this.createdAt,
        isDeleted = this.isDeleted
    )
}

/**
 * SalePaymentMapper
 */

fun SalePaymentEntity.toDomain(): SalePayment {
    return SalePayment(
        id = this.id,
        sn = this.sn ?: "PAY-N/A",
        invoiceId = this.invoiceId,
        customerId = this.customerId,
        // 如果 Entity 是 Int，转换为 Domain 的 Long
        amount = this.amount.toLong(),
        paymentTime = this.paymentTime,
        paymentMethods = this.paymentMethods
    )
}

fun SalePayment.toEntity(): SalePaymentEntity {
    return SalePaymentEntity(
        id = this.id,
        sn = this.sn,
        invoiceId = this.invoiceId,
        customerId = this.customerId,
        amount = this.amount,
        paymentTime = this.paymentTime,
        paymentMethods = this.paymentMethods
    )
}

/**
 * PurchaseInvoiceMapper
 */

fun PurchaseInvoiceEntity.toDomain(): PurchaseInvoice {
    return PurchaseInvoice(
        id = this.id,
        sn = this.sn ?: "P-INV-N/A",
        supplierId = this.supplierId,
        supplierName = this.supplierName,
        creatorId = this.creatorId,
        creatorName = this.creatorName,
        amountPayable = this.amountPayable,
        amountPaid = this.amountPaid,
        amountRem = this.amountRem,
        status = this.status,
        remark = this.remark ?: "",
        createdAt = this.createdAt,
        isDeleted = this.isDeleted
    )
}

fun PurchaseInvoice.toEntity(): PurchaseInvoiceEntity {
    return PurchaseInvoiceEntity(
        id = this.id,
        sn = this.sn,
        supplierId = this.supplierId,
        supplierName = this.supplierName,
        creatorId = this.creatorId,
        creatorName = this.creatorName,
        amountPayable = this.amountPayable,
        amountPaid = this.amountPaid,
        amountRem = this.amountRem,
        status = this.status,
        remark = this.remark,
        createdAt = this.createdAt,
        isDeleted = this.isDeleted
    )
}

/**
 * CombinedInvoiceMapper
 */

fun CombinedInvoiceView.toDomain(): CombinedInvoice {
    return CombinedInvoice(
        symbol = this.symbol,
        id = this.id,
        sn = this.sn ?: "N/A",
        partnerId = this.partnerId,
        partnerName = this.partnerName,
        creatorId = this.creatorId,
        creatorName = this.creatorName,
        amountTotal = this.amountTotal,
        amountPaid = this.amountPaid,
        amountRem = this.amountRem,
        status = this.status,
        remark = this.remark ?: "",
        createdAt = this.createdAt
    )
}
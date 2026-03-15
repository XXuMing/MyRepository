package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.utils.PaymentMethods

/**
 * 采购流水
 * @param id 主键
 * @param sn 采购流水编号
 * @param invoiceId 账单ID
 * @param supplierId 供应商ID
 * @param amount 金额
 * @param paymentTime 付款时间
 */
@Entity(
    tableName = "purchase_payment",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseInvoiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["invoice_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("sn", unique = true),
        Index("invoice_id"),
        Index("supplier_id"),
        Index("payment_time")
    ]
)
data class PurchasePaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "sn")
    val sn: String ?= null,

    @ColumnInfo(name = "invoice_id")
    val invoiceId: Long,

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long,

    @ColumnInfo(name = "amount")
    val amount: Long,

    @ColumnInfo(name = "payment_time")
    val paymentTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "payment_methods")
    val paymentMethods : PaymentMethods,
)
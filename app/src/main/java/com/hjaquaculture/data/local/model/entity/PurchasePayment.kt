package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 采购流水
 * @param id 主键
 * @param billId 账单ID
 * @param supplierId 供应商ID
 * @param amount 金额
 * @param paymentTime 付款时间
 */
@Entity(
    tableName = "purchase_payment",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseBill::class,
            parentColumns = ["id"],
            childColumns = ["bill_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("bill_id"),
        Index("supplier_id"),
        Index("payment_time")
    ]
)
data class PurchasePayment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "bill_id")
    val billId: Long,

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "payment_time")
    val paymentTime: Long = System.currentTimeMillis()
)
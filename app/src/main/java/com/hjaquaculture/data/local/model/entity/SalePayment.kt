package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 销售流水
 * @param id 主键
 * @param invoiceId 发票ID
 * @param customerId 客户ID
 * @param amount 金额
 * @param paymentTime 付款时间
 */
@Entity(
    tableName = "sale_payment",
    foreignKeys =[
        ForeignKey(
            entity = SaleInvoice::class,
            parentColumns = ["id"],
            childColumns = ["invoice_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("invoice_id"),
        Index("customer_id"),
        Index("payment_time")
    ]
)
data class SalePayment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "invoice_id")
    val invoiceId: Long,

    @ColumnInfo(name = "customer_id")
    val customerId: Long = 0,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "payment_time")
    val paymentTime: Long = System.currentTimeMillis()
)
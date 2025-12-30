package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 销售发票
 * @param id 主键
 * @param orderId 销售订单ID
 * @param customerId 客户ID
 * @param userId 用户ID
 * @param amountDue 应付金额
 * @param amountPaid 实付金额
 * @param status 状态(已付，未付，部分支付，作废)
 * @param remark 备注
 * @param createdAt 创建时间
 */
@Entity(
    tableName = "sale_invoice",
    foreignKeys = [
        ForeignKey(
            entity = SaleOrder::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customer_id"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("order_id"),
        Index("customer_id"),
        Index("user_id"),
        Index("status"),
        Index("created_at")
    ]
)
data class SaleInvoice(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "order_id")
    val orderId: Long,

    @ColumnInfo(name = "customer_id")
    val customerId: Long,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "amount_due")
    val amountDue: Double,

    @ColumnInfo(name = "amount_paid")
    val amountPaid: Double,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "remark")
    val remark: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()

)
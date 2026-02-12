package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.utils.InvoiceStatus

/**
 * 采购账单
 * @param id 主键
 * @param sn 采购账单编号
 * @param orderId 采购订单ID
 * @param supplierId 供应商ID
 * @param userId 用户ID
 * @param amountPayable 应付金额
 * @param amountPaid 实付金额
 * @param amountRem 欠款金额
 * @param status 状态(已付，未付，部分支付，作废)
 * @param remark 备注
 * @param createdAt 创建时间
 */
@Entity(
    tableName = "purchase_invoice",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseOrder::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Supplier::class,
            parentColumns = ["id"],
            childColumns = ["supplier_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("sn",unique = true),
        Index("order_id"),
        Index("supplier_id"),
        Index("user_id"),
        Index("status"),
        Index("created_at")
    ]
)
data class PurchaseInvoice (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "sn")
    val sn: String ?= null,

    @ColumnInfo(name = "order_id")
    val orderId: Long,

    @ColumnInfo(name = "order_sn")
    val orderSn:String,

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long,

    @ColumnInfo(name = "supplier_name")
    val supplierName: String,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "amount_payable")
    val amountPayable: Int,

    @ColumnInfo(name = "amount_paid")
    val amountPaid: Int,

    @ColumnInfo(name = "amount_rem")
    val amountRem: Long,

    @ColumnInfo(name = "status")
    val status: InvoiceStatus = InvoiceStatus.UNPAID,

    @ColumnInfo(name = "remark")
    val remark: String ?= null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
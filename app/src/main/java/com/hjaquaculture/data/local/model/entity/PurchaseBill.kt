package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 采购账单
 * @param id 主键
 * @param purchaseId 采购订单ID
 * @param supplierId 供应商ID
 * @param userId 用户ID
 * @param amountPayable 应付金额
 * @param amountPaid 实付金额
 * @param status 状态(已付，未付，部分支付，作废)
 * @param remark 备注
 * @param createdAt 创建时间
 */
@Entity(
    tableName = "purchase_bill",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseOrder::class,
            parentColumns = ["id"],
            childColumns = ["purchase_id"],
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
        Index("purchase_id"),
        Index("supplier_id"),
        Index("user_id"),
        Index("status"),
        Index("created_at")
    ]
)
data class PurchaseBill (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "purchase_id")
    val purchaseId: Long,

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "amount_payable")
    val amountPayable: Double,

    @ColumnInfo(name = "amount_paid")
    val amountPaid: Double,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "remark")
    val remark: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
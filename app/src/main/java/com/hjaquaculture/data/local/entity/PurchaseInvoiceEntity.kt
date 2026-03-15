package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.utils.InvoiceStatus

/**
 * 采购账单
 * @param id 主键
 * @param sn 采购账单编号
 * @param supplierId 供应商ID
 * @param supplierName 供应商名称
 * @param creatorId 操作者ID
 * @param creatorName 操作者名称
 * @param amountPayable 应付金额
 * @param amountPaid 实付金额
 * @param amountRem 欠款金额
 * @param status 状态(已付，未付，部分支付，作废)
 * @param remark 备注
 * @param createdAt 创建时间
 * @param isDeleted 是否删除
 */
@Entity(
    tableName = "purchase_invoice",
    foreignKeys = [
        ForeignKey(
            entity = SupplierEntity::class,
            parentColumns = ["id"],
            childColumns = ["supplier_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creator_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["id", "supplier_id"], unique = true),
        Index("sn",unique = true),
        Index("supplier_id"),
        Index("creator_id"),
        Index("status"),
        Index("created_at")
    ]
)
data class PurchaseInvoiceEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "sn")
    val sn: String ?= null,

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long,

    @ColumnInfo(name = "supplier_name")
    val supplierName: String,

    @ColumnInfo(name = "creator_id")
    val creatorId: Long,

    @ColumnInfo(name = "creator_name")
    val creatorName: String,

    @ColumnInfo(name = "amount_payable")
    val amountPayable: Long,

    @ColumnInfo(name = "amount_paid")
    val amountPaid: Long,

    @ColumnInfo(name = "amount_rem")
    val amountRem: Long,

    @ColumnInfo(name = "status")
    val status: InvoiceStatus = InvoiceStatus.UNPAID,

    @ColumnInfo(name = "remark")
    val remark: String ?= null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
)
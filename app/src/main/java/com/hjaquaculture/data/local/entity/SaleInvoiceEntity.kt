package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.base.InvoiceStatus

/**
 * 销售发票
 * @param id 主键
 * @param sn 销售发票编号
 * @param customerId 客户ID
 * @param customerName 客户名称
 * @param creatorId 操作者ID
 * @param creatorName 操作者名称
 * @param amountDue 应付金额,作为快照功能，在创建账单后直接从Order表中赋值过来后就不允许修改
 * @param amountPaid 实付金额，每次新增流水都会更新
 * @param amountRem 欠款金额，每次新增流水都会更新
 * @param status 状态(已付，未付，部分支付，作废)
 * @param remark 备注
 * @param createdAt 创建时间
 * @param isDeleted 是否删除
 */
@Entity(
    tableName = "sale_invoice",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customer_id"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creator_id"],
            onUpdate = ForeignKey.NO_ACTION,
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["id", "customer_id"], unique = true),
        Index("sn", unique = true),
        Index("customer_id"),
        Index("creator_id"),
        Index("status"),
        Index("created_at")
    ]
)
data class SaleInvoiceEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "sn")
    val sn: String ?= null,

    @ColumnInfo(name = "customer_id")
    val customerId: Long,

    @ColumnInfo(name = "customer_name")
    val customerName: String,

    @ColumnInfo(name = "creator_id")
    val creatorId: Long,

    @ColumnInfo(name = "creator_name")
    val creatorName: String,

    @ColumnInfo(name = "amount_due")
    val amountDue: Long,

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
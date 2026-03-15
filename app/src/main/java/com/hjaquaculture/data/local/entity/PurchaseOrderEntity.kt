package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.utils.OrderStatus
import com.hjaquaculture.common.utils.OrderType

/**
 * 采购订单
 * @param id 主键
 * @param sn 采购订单编号
 * @param invoiceId 账单ID
 * @param invoiceSn 账单编号
 * @param supplierId 供应商ID
 * @param creatorId 用户ID
 * @param orderType 订单类型（货运、自提）
 * @param orderStatus 订单状态（预定、草稿、确认、完成、作废）
 * @param totalPrice 总价(元)
 * @param totalQuantity 总数量
 * @param expiredAt 审核时间
 * @param remark 备注
 * @param createdAt 创建时间
 * @param isDeleted 是否删除
 */
@Entity(
    tableName = "purchase_order",
    foreignKeys =[
        ForeignKey(
            entity = PurchaseInvoiceEntity::class,
            parentColumns = ["id","supplier_id"],// 复合主键
            childColumns = ["invoice_id","supplier_id"],// 复合主键
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
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
        Index("sn",unique = true),
        Index(value = ["invoice_id","supplier_id"]),
        Index("supplier_id"),
        Index("creator_id"),
        Index("order_type"),
        Index("order_status"),
        Index("created_at")
    ]
)
data class PurchaseOrderEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "sn")
    val sn: String ?= null,

    @ColumnInfo(name = "invoice_id")
    val invoiceId: Long ?= null,

    @ColumnInfo(name = "invoice_sn")
    val invoiceSn: String ?= null,

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long,

    @ColumnInfo(name = "supplier_name")
    val supplierName: String,

    @ColumnInfo(name = "creator_id")
    val creatorId: Long,

    @ColumnInfo(name = "order_type")
    val orderType: OrderType,

    @ColumnInfo(name = "order_status")
    val orderStatus: OrderStatus = OrderStatus.DRAFT,

    @ColumnInfo(name = "total_price")
    val totalPrice: Long,

    @ColumnInfo(name = "total_quantity")
    val totalQuantity: Int,

    @ColumnInfo(name = "remark")
    val remark: String ?= null,

    @ColumnInfo(name = "expired_at")
    val expiredAt: Long ?= null,

    @ColumnInfo(name = "created_at")
    val createdAt : Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
)
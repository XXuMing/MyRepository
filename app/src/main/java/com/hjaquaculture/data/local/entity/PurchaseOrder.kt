package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.utils.PurchaseOrderStatus
import com.hjaquaculture.common.utils.PurchaseOrderType

/**
 * 采购订单
 * @param id 主键
 * @param sn 采购订单编号
 * @param supplierId 供应商ID
 * @param userId 用户ID
 * @param orderType 订单类型(收寄货、自采购)
 * @param status 订单状态(审核、未审核)
 * @param totalPrice 总价(元)
 * @param totalQuantity 总数量
 * @param auditedAt 审核时间
 * @param remark 备注
 * @param orderDate 订单时间
 */
@Entity(
    tableName = "purchase_order",
    foreignKeys =[
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
        Index("supplier_id"),
        Index("user_id"),
        Index("order_type"),
        Index("status"),
        Index("order_date")
    ]
)
data class PurchaseOrder (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "sn")
    val sn: String ?= null,

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long,

    @ColumnInfo(name = "supplier_name")
    val supplierName: String,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "order_type")
    val orderType: PurchaseOrderType,

    @ColumnInfo(name = "status")
    val status: PurchaseOrderStatus = PurchaseOrderStatus.UNAUDITED,

    @ColumnInfo(name = "total_price")
    val totalPrice: Long,

    @ColumnInfo(name = "total_quantity")
    val totalQuantity: Int,

    @ColumnInfo(name = "audited_at")
    val auditedAt: Long = 0,

    @ColumnInfo(name = "remark")
    val remark: String ?= null,

    @ColumnInfo(name = "order_date")
    val orderDate : Long = System.currentTimeMillis(),

)
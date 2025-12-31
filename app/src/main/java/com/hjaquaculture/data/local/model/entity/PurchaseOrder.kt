package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.data.local.model.entity.status.PurchaseOrderStatus
import com.hjaquaculture.data.local.model.entity.status.PurchaseOrderType

/**
 * 采购订单
 * @param id 主键
 * @param supplierId 供应商ID
 * @param userId 用户ID
 * @param orderType 订单类型(收寄货、自采购)
 * @param status 订单状态(审核、未审核)
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

    @ColumnInfo(name = "supplier_id")
    val supplierId: Long,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "order_type")
    val orderType: PurchaseOrderType,

    @ColumnInfo(name = "status")
    val status: PurchaseOrderStatus,

    @ColumnInfo(name = "audited_at")
    val auditedAt: Long = 0,

    @ColumnInfo(name = "remark")
    val remark: String,

    @ColumnInfo(name = "order_date")
    val orderDate : Long = System.currentTimeMillis()

)
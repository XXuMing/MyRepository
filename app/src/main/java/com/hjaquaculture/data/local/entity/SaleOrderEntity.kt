package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.base.DeliveryMethod
import com.hjaquaculture.common.base.OrderStatus

/**
 * 销售订单
 * @param id 主键
 * @param sn 销售订单编号
 * @param invoiceId 账单ID
 * @param invoiceSn 账单编号
 * @param creatorId 用户ID
 * @param customerId 客户ID
 * @param deliveryMethod 订单类型（货运、自提）
 * @param orderStatus 订单状态（预定、草稿、确认、完成、作废）
 * @param totalPrice 总价（元）
 * @param totalQuantity 总数量
 * @param expiredAt 预定处理时间
 * @param remark 备注
 * @param createdAt 创建时间
 * @param completedAt 完成时间
 * @param isDeleted 是否删除
 */
@Entity(
    tableName = "sale_order",
    foreignKeys = [
        ForeignKey(
            entity = SaleInvoiceEntity::class,
            parentColumns = ["id","customer_id"], // 复合主键
            childColumns = ["invoice_id","customer_id"], // 复合主键
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creator_id"]
        ),
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customer_id"]
        )
    ],
    indices = [//后期想想能不能优化一下这里的索引
        Index("sn", unique = true),
        Index(value = ["invoice_id","customer_id"]),
        Index("creator_id"),
        Index("customer_id"),
        //Index(value = ["order_type"]),
        //Index(value = ["status"]),
        Index("created_at"),
        Index("expired_at"),
        Index(value = ["order_type", "order_status"]),
    ]
)
data class SaleOrderEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "sn")
    val sn: String ?= null,

    @ColumnInfo(name = "invoice_id")
    val invoiceId: Long ?= null,

    @ColumnInfo(name = "invoice_sn")
    val invoiceSn: String ?= null,

    @ColumnInfo(name = "creator_id")
    val creatorId: Long,

    @ColumnInfo(name = "customer_id")
    val customerId: Long,

    @ColumnInfo(name = "order_type")
    val deliveryMethod: DeliveryMethod,

    @ColumnInfo(name = "order_status")
    var orderStatus: OrderStatus,

    @ColumnInfo(name = "total_price")
    val totalPrice: Long = 0,

    @ColumnInfo(name = "total_quantity")
    val totalQuantity: Int = 0,

    @ColumnInfo(name = "remark")
    val remark: String ?= null,

    @ColumnInfo(name = "expired_at")
    val expiredAt : Long ?= null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "completed_at")
    val completedAt: Long? = null,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
)

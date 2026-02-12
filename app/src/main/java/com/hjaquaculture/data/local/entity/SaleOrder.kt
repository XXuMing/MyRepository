package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.hjaquaculture.common.utils.SaleOrderStatus
import com.hjaquaculture.common.utils.SaleOrderType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 销售订单
 * @param id 主键
 * @param sn 销售订单编号
 * @param userId 用户ID
 * @param customerId 客户ID
 * @param orderType 订单类型（寄货单、自提单）
 * @param status 订单状态（未付、已付、取消、待处理、已处理未付）
 * @param totalPrice 总价（元）
 * @param totalQuantity 总数量
 * @param remark 备注
 * @param createdAt 创建时间
 * @param timeLimit 未处理订单的预计处理时间
 */
@Entity(
    tableName = "sale_order",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"]
        ),
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customer_id"]
        )
    ],
    indices = [//后期想想能不能优化一下这里的索引
        Index(value = ["sn"], unique = true),
        Index(value = ["user_id"]),
        Index(value = ["customer_id"]),
        //Index(value = ["order_type"]),
        //Index(value = ["status"]),
        Index(value = ["created_at"]),
        Index(value = ["time_limit"]),
        Index(value = ["order_type", "status"])
    ]
)
data class SaleOrder (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "sn")
    val sn: String ?= null,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "customer_id")
    val customerId: Long,

    @ColumnInfo(name = "order_type")
    val orderType: SaleOrderType,

    @ColumnInfo(name = "status")
    var status: SaleOrderStatus,

    @ColumnInfo(name = "total_price")
    val totalPrice: Long,

    @ColumnInfo(name = "total_quantity")
    val totalQuantity: Int,

    @ColumnInfo(name = "remark")
    val remark: String ?= null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "time_limit")
    val timeLimit : Long = System.currentTimeMillis(),

)

/**
 * 销售订单明细
 */
data class SaleOrderWithItems(
    @Embedded
    val saleOrder: SaleOrder,

    @Relation(
        parentColumn = "id",
        entityColumn = "sale_order")
    val items: List<SaleOrderItem>
)
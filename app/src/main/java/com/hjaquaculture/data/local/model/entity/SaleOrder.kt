package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.data.local.model.entity.status.SaleOrderStatus
import com.hjaquaculture.data.local.model.entity.status.SaleOrderType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 销售订单
 * @param id 主键
 * @param userId 用户ID
 * @param customerId 客户ID
 * @param orderType 订单类型（寄货单、自提单）
 * @param status 订单状态（未付、已付、取消、待处理、已处理未付）
 * @param remark 备注
 * @param createdAt 创建时间
 * @param processingDate 处理时间
 *
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
        Index(value = ["user_id"]),
        Index(value = ["customer_id"]),
        //Index(value = ["order_type"]),
        //Index(value = ["status"]),
        Index(value = ["created_at"]),
        Index(value = ["processing_date"]),
        Index(value = ["order_type", "status"])
    ]
)
data class SaleOrder (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long = 0,

    @ColumnInfo(name = "customer_id")
    val customerId: Long = 0,

    @ColumnInfo(name = "order_type")
    val orderType: SaleOrderType,

    @ColumnInfo(name = "status")
    var status: SaleOrderStatus,

    @ColumnInfo(name = "remark")
    val remark: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "processing_date")
    val processingDate : String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

)


/*
// 在 ViewModel 或 UI 中处理 订单类型的 逻辑
when (order.status) {
    OrderStatus.PICKUP_UNPAID -> showPayButton()
    OrderStatus.SHIPPING_UNPAID_PROCESSED -> showLogisticsInfo()
    OrderStatus.CANCELLED -> showReorderButton()
    else -> hideAllButtons()
}
*/
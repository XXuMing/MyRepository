package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 销售订单明细
 * @param id 主键
 * @param orderId 销售订单ID
 * @param productId 商品ID
 * @param quantity 数量
 * @param unit 单位（斤） 允许自填
 * @param unitPrice 单价（元）
 * @param price 价格（元）
 */
@Entity(
    tableName = "sale_order_item",
    foreignKeys = [
        ForeignKey(
            entity = SaleOrder::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION

        )
    ],
    indices = [
        Index(value = ["order_id"])
    ]
)
data class SaleOrderItem(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "order_id")
    val orderId : Long,

    @ColumnInfo(name = "product_id")
    val productId : Long,

    @ColumnInfo(name = "quantity")
    val quantity : Int,

    @ColumnInfo(name = "unit")
    val unit : String,

    @ColumnInfo(name = "unit_price")
    val unitPrice : Int,

    @ColumnInfo(name = "price")
    val price : Int
)
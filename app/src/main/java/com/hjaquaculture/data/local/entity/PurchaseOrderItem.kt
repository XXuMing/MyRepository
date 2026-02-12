package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 采购订单明细
 * @param id 主键
 * @param orderId 采购订单ID
 * @param productId 商品ID
 * @param productName 商品名称
 * @param quantity 数量
 * @param unit 单位（件、箱、袋）
 * @param weight 重量（斤）
 * @param unitPrice 单价（元）
 * @param subtotal 小计（元）
 */
@Entity(
    tableName = "purchase_order_item",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseOrder::class,
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
        Index("order_id"),
        Index("product_id")
    ]
)
data class PurchaseOrderItem (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "order_id")
    val orderId: Long,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "product_name")
    val productName: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "unit")
    val unit : String,

    @ColumnInfo(name = "weight")
    val weight: Int,

    @ColumnInfo(name = "unit_price")
    val unitPrice: Long,

    @ColumnInfo(name = "subtotal")
    val subtotal: Long

)
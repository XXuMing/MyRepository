package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.base.StockUnit

/**
 * 采购订单明细
 * @param id 主键
 * @param orderId 采购订单ID
 * @param productId 商品ID
 * @param productName 商品名称
 * @param quantity 数量
 * @param quantityUnit 数量单位（件、箱、袋）
 * @param weight 重量（斤）
 * @param weightUnit 重量单位（斤）
 * @param unitPrice 单价（元）
 * @param subtotal 小计（元）
 */
@Entity(
    tableName = "purchase_order_item",
    foreignKeys = [
        ForeignKey(
            entity = PurchaseOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index("order_id"),
        Index("product_id"),
    ]
)
data class PurchaseOrderItemEntity (
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

    @ColumnInfo(name = "quantity_unit")
    val quantityUnit: StockUnit,

    @ColumnInfo(name = "weight")
    val weight: Int,

    @ColumnInfo(name = "weight_unit")
    val weightUnit : StockUnit,

    @ColumnInfo(name = "unit_price")
    val unitPrice: Long,

    @ColumnInfo(name = "subtotal")
    val subtotal: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
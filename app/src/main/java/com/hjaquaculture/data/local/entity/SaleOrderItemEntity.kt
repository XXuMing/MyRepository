package com.hjaquaculture.data.local.entity

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
 * @param productName 商品名称(快照)
 * @param quantity 数量
 * @param quantityUnitId 数量单位（件、箱、袋）
 * @param weight 重量（斤）
 * @param weightUnitId 重量单位（斤）
 * @param unitPrice 单价（元）
 * @param subtotal 小计（元）
 */
@Entity(
    tableName = "sale_order_item",
    foreignKeys = [
        ForeignKey(
            entity = SaleOrderEntity::class,
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
        ),
        ForeignKey(
            entity = MeasureUnitEntity::class,
            parentColumns = ["id"],
            childColumns = ["quantity_unit_id"],
        ),
        ForeignKey(
            entity = MeasureUnitEntity::class,
            parentColumns = ["id"],
            childColumns = ["weight_unit_id"]
        )
    ],
    indices = [
        Index(value = ["order_id"]),
        Index(value = ["product_id"]),
        Index(value = ["quantity_unit_id"]),
        Index(value = ["weight_unit_id"])
    ]
)
data class SaleOrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "order_id")
    val orderId : Long,

    @ColumnInfo(name = "product_id")
    val productId : Long,

    @ColumnInfo(name = "product_name")
    val productName : String,

    @ColumnInfo(name = "quantity")
    val quantity : Int,

    @ColumnInfo(name = "quantity_unit_id")
    val quantityUnitId : Int,

    @ColumnInfo(name = "weight")
    val weight : Int,

    @ColumnInfo(name = "weight_unit_id")
    val weightUnitId : Int,

    @ColumnInfo(name = "unit_price")
    val unitPrice : Long,

    @ColumnInfo(name = "subtotal")
    val subtotal: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()

)
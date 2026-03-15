package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
 * 实时库存
 * @param id 主键
 * @param productId 商品ID
 * @param quantity 当前库存总量
 * @param minStock 预警水位
 */
/*
@Entity(
    tableName = "inventory",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        )
    ],
    indices = [Index("product_id", unique = true)]
)
*/

data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    // 当前库存总量（以基础单位计，如“个”）
    @ColumnInfo(name = "quantity")
    val quantity: Int = 0,

    // 预警水位：低于此值时 UI 提醒
    @ColumnInfo(name = "min_stock")
    val minStock: Int = 0,

    @ColumnInfo(name = "last_updated_at")
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
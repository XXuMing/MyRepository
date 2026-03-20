package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 实时库存
 * @param id 主键
 * @param productId 商品ID
 * @param amount 库存量
 * @param minStock 预警水位
 * @param lastUpdatedAt 最后更新时间
 */

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


data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "amount")
    val amount: Int = 0,

    @ColumnInfo(name = "min_stock")
    val minStock: Int = 0,

    @ColumnInfo(name = "last_updated_at")
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
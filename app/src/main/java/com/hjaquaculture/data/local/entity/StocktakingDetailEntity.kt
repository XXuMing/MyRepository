package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 盘点明细
 * @param id 主键
 * @param stocktakingId 盘点单ID
 * @param productId 商品ID
 * @param systemAmount 盘点发起时的系统库存快照
 * @param actualAmount 实物清点数量，null表示尚未录入
 * @param difference 差异 = actual - system，正数盘盈，负数盘亏
 * @param remark 备注
 */
@Entity(
    tableName = "stocktaking_item",
    foreignKeys = [
        ForeignKey(
            entity = StocktakingEntity::class,
            parentColumns = ["id"],
            childColumns = ["stocktaking_id"],
            onDelete = ForeignKey.CASCADE   // 盘点单删除，明细一并删除
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["stocktaking_id", "product_id"], unique = true),
        // 同一张盘点单里，同一个商品只能有一条明细
        Index("product_id")
    ]
)
data class StocktakingDetailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "stocktaking_id")
    val stocktakingId: Long,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "system_amount")
    val systemAmount: Int,          // 盘点发起时的系统库存快照

    @ColumnInfo(name = "actual_amount")
    val actualAmount: Int? = null,  // 实物清点数量，null表示尚未录入

    @ColumnInfo(name = "difference")
    val difference: Int? = null,    // 差异 = actual - system，正数盘盈，负数盘亏

    @ColumnInfo(name = "remark")
    val remark: String? = null
)
package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 商品价格历史记录
 * @param id 主键
 * @param productId 商品ID
 * @param price 价格
 * @param operatorId 操作员ID
 * @param changedAt 价格变更时间
 * @param remark 备注
 */
@Entity(
    tableName = "product_price_history",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["operator_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["product_id"]),
        Index(value = ["operator_id"]),
        Index(value = ["changed_at"])
    ]
)
data class ProductPriceHistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "price")
    val price: Long,

    @ColumnInfo(name = "operator_id")
    val operatorId: Long,

    @ColumnInfo(name = "changed_at")
    val changedAt : Long = System.currentTimeMillis(),

    @ColumnInfo(name = "remark")
    val remark: String? = null,
)

/*
// // 第一条是新价，第二条就是原价
// 在 DAO 里提供这个查询：
@Query("""
    SELECT * FROM product_price_history
    WHERE product_id = :productId
    ORDER BY changed_at DESC
    LIMIT 2
""")
suspend fun getLatestTwo(productId: Long): List<ProductPriceHistoryEntity>
 */
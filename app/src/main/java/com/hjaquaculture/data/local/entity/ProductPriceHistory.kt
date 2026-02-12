package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 商品价格历史记录
 * @param id 主键
 * @param productId 商品ID
 * @param userId 用户ID
 * @param originalPrice 原价
 * @param originalPriceDate 原价日期
 * @param newPrice 新价
 * @param newPriceDate 新价日期
 */
@Entity(
    tableName = "product_price_history",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["product_id"]
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"]
        )
    ],
    indices = [
        // 由于这里的 最新价格 是唯一索引，但暂时还没想好如何获取违反索引约束的错误警告。
        Index(value = ["new_price_date", "product_id"], unique = true),
        Index(value = ["product_id"]),
        Index(value = ["user_id"])
    ]
)
data class ProductPriceHistory (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    //在移动端（Room/SQLite），Double 存在精度丢失风险。
    //专业做法：将价格乘以 100 存储为 Long。例如：19.99 元存为 1999。在显示时再除以 100。这样可以避免 19.99 + 0.01 = 19.999999999 的尴尬。
    @ColumnInfo(name = "original_price")
    val originalPrice: Long,

    @ColumnInfo(name = "original_price_date")
    val originalPriceDate : String,

    @ColumnInfo(name = "new_price")
    val newPrice: Long,

    @ColumnInfo(name = "new_price_date")
    val newPriceDate : String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
)
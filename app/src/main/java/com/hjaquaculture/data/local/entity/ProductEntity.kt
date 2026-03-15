package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * 商品
 * @param id 主键
 * @param name 商品名
 * @param currentPrice 当前价格
 * @param categoryId 分类
 * @param sort 排序
 */
@Entity(
    tableName = "product",
    foreignKeys =[
        ForeignKey(
            entity = ProductCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["name"], unique = true),
        Index(name="is_available_index",value = ["is_available"]),
        Index(name="index_product_category_id_sort",value = ["category_id","sort"], orders = [Index.Order.ASC, Index.Order.ASC])
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "name")
    val name : String,

    //注意：由于存在“价格表”，当修改价格时，应该在一个 事务 (Transaction) 中执行两步：
    //更新 product 表的 current_price。
    //以及向 product_price_logs 插入一条新记录。
    @ColumnInfo(name = "current_price")
    val currentPrice : Long = 0,

    @ColumnInfo(name = "category_id")
    val categoryId : Long,

    @ColumnInfo(name = "is_available")
    val isAvailable : Boolean = true,

    @ColumnInfo(name = "sort")
    val sort : Int = 0,
)


data class ProductWithCategoryEntity(
    @Embedded val product: ProductEntity,
    @Relation(parentColumn = "category_id", entityColumn = "id")
    val category: ProductCategoryEntity
)

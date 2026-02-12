package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * 商品分类
 * @param id 主键
 * @param name 分类名
 * @param sort 排序
 */
@Entity(
    tableName = "product_category",
    indices =[
        Index(value = ["name"], unique = true),
        Index(value = ["sort"], orders = [Index.Order.ASC])
    ]
)
data class ProductCategory(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "name")
    val name : String,

    @ColumnInfo(name = "sort")
    val sort : Int = 0,

)

/**
 * 商品分类与商品的关系
 */
data class ProductCategoryWithProducts(
    @Embedded val category: ProductCategory,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val products: List<Product>
)
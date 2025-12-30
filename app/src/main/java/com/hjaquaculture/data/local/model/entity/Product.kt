package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 商品
 * @param id 主键
 * @param name 商品名
 * @param currentPrice 当前价格
 * @param category 分类
 * @param sort 排序
 */
@Entity(
    tableName = "product",
    indices = [
        Index("name", unique = true),
        Index(value = ["category","sort"], orders = [Index.Order.ASC, Index.Order.ASC])
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "name")
    val name : String,

    //注意：由于存在“价格表”，当修改价格时，应该在一个 事务 (Transaction) 中执行两步：
    //更新 product 表的 current_price。
    //以及向 product_price_logs 插入一条新记录。
    @ColumnInfo(name = "current_price")
    val currentPrice : Int,

    @ColumnInfo(name = "category")
    val category : String,

    @ColumnInfo(name = "sort")
    val sort : Int,
)
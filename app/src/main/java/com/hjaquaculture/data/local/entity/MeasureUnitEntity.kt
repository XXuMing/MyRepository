package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 单位表
 * @param id 主键
 * @param name 名称
 * @param category 分类
 * @param conversionRate 换算率
 * @param precision 小数位数
 * @param isBase 是否基准，每个分类中只能存在一个基准
 * @param sort 排序权重
 */
@Entity(
    tableName = "measure_units",
    indices = [
        Index(value = ["category", "is_base"], unique = true),
        Index(value = ["category","name"]),
        Index(value = ["sort"]),
    ]
)
data class MeasureUnitEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "conversion_rate")
    val conversionRate: Double,

    @ColumnInfo(name = "precision")
    val precision: Int,

    @ColumnInfo(name = "is_base")
    val isBase: Boolean,

    @ColumnInfo(name = "sort")
    val sort: Int,
)
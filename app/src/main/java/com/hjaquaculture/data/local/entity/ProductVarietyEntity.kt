package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.hjaquaculture.data.local.mapper.toDomain
import com.hjaquaculture.domain.model.VarietyWithProducts

/**
 * 商品品类
 * @param id 主键
 * @param name 品类名
 * @param sort 排序
 */
@Entity(
    tableName = "product_variety",
    indices =[
        Index(value = ["name"], unique = true),
        Index(value = ["sort"], orders = [Index.Order.ASC])
    ]
)
data class ProductVarietyEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "name")
    val name : String,

    @ColumnInfo(name = "sort")
    val sort : Int = 0,

)

/**
 * Data 层专用：用于 Room 关联查询的实体
 */
data class VarietyWithProductEntity(
    @Embedded
    val category: ProductVarietyEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val products: List<ProductEntity>
)

/**
 * 将 Room 查询出的关联实体转换为 Domain 层的业务对象
 */
fun VarietyWithProductEntity.toDomain(): VarietyWithProducts {
    return VarietyWithProducts(
        category = this.category.toDomain(), // 调用之前写的 CategoryMapper
        products = this.products.map { it.toDomain() } // 遍历并将每个 ProductEntity 转为 Product
    )
}
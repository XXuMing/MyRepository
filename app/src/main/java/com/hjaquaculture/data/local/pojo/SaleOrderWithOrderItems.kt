package com.hjaquaculture.data.local.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.hjaquaculture.data.local.entity.SaleOrderEntity
import com.hjaquaculture.data.local.entity.SaleOrderItemEntity


data class SaleOrderWithOrderItems(
    @Embedded val saleOrderEntity: SaleOrderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "sale_order_id",
        entity = SaleOrderItemEntity::class
    )
    val orderItems: List<SaleOrderItemEntity>
)
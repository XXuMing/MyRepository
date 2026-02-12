package com.hjaquaculture.data.local.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.hjaquaculture.data.local.entity.SaleOrder
import com.hjaquaculture.data.local.entity.SaleOrderItem


data class SaleOrderWithOrderItems(
    @Embedded val saleOrder: SaleOrder,

    @Relation(
        parentColumn = "id",
        entityColumn = "sale_order_id",
        entity = SaleOrderItem::class
    )
    val orderItems: List<SaleOrderItem>
)
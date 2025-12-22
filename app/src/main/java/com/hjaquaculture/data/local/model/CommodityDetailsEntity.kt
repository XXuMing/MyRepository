package com.hjaquaculture.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * 商品明细
 */
@Entity(tableName = "commodityDetails_table")
data class CommodityDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val commodityList : List<CommodityEntity>,
    val createdAt : Instant,
    val operationLog : OperationEntity,
    val remark : String,
    val state : State
){
    object State {
        // 无
        const val NONE = 0;
        // 未付款
        const val UNPAID = 1;
        // 已付款
        const val PAID = 2;
        // 待处理
        const val PENDING = 3;
    }
}
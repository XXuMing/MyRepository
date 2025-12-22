package com.hjaquaculture.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 商品
 */
@Entity(tableName = "commodity_table")
data class CommodityEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val name : String,
    val classification : String,
    val sort : String,
    val price : Int,
    val amount : Int,
    val unit : String
)
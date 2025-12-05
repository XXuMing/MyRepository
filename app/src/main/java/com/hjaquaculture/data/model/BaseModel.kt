package com.hjaquaculture.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户
 */
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    val creationTime : String,
    val password : String
)

/**
 * 客户
 */
@Entity(tableName = "customers_table")
data class Customers(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    val password : String,
    var nikeName : String,
    var phoneNumber: Number,
    val creationTime : String,
    val address : String
)

/**
 * 商品
 */
@Entity(tableName = "commodity_table")
data class Commodity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    val classification : String,
    val sort : String,
    val price : Int,
    val amount : Int,
    val unit : String
)

/**
 * 操作记录
 */
@Entity(tableName = "operation_table")
data class Operation(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val operator : User,
    val time : String,
    val log : String
)

/**
 * 商品明细
 */
@Entity(tableName = "commodityDetails_table")
data class CommodityDetails(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val commodityList : List<Commodity>,
    val creationTime : String,
    val operationLog : Operation,
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
/**
 *
 */
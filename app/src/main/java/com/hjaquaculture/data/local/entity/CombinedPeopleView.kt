package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.hjaquaculture.common.utils.PeopleSymbol

/**
 * 合并人员视图
 * @param symbol 标识
 * @param id 主键
 * @param name 名称
 * @param phone 手机号
 * @param account 账号
 * @param nikeName 昵称
 * @param role 角色
 * @param address 地址
 * @param createdAt 创建时间
 */
@DatabaseView(
    viewName = "combined_people_view",
    value = """
SELECT 'USER' AS symbol ,
    id , name , phone , created_at , address ,
    account ,
    NULL AS nike_name ,
    role 
FROM user

UNION ALL
SELECT 'CUSTOMER' AS symbol ,
    id , name , phone , created_at , address ,
    NULL AS account ,
    nick_name ,
    NULL AS role 
FROM customer

UNION ALL
SELECT 'SUPPLIER' AS symbol ,
    id , name , phone , created_at , address ,
    NULL AS account ,
    NULL AS nike_name ,
    NULL AS role 
FROM supplier
"""
)
data class CombinedPeopleView(
    @ColumnInfo("symbol")
    val symbol: PeopleSymbol,

    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("phone")
    val phone: String?,

    @ColumnInfo("account")
    val account: String?,

    @ColumnInfo("nike_name")
    val nikeName: String?,

    @ColumnInfo("role")
    val role: String?,

    @ColumnInfo("address")
    val address: String?,

    @ColumnInfo("created_at")
    val createdAt: Long
)

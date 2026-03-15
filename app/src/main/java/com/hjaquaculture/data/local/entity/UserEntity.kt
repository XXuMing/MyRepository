package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 用户
 * @param id 主键
 * @param account 账号
 * @param name 用户名
 * @param phone 电话
 * @param createdAt 创建时间
 * @param role 角色
 * @param passwordHash 密码的哈希值
 * @param address 地址
 */
@Entity(
    tableName = "user",
    indices = [
        Index("account", unique = true,),
        Index("name", unique = true),
        Index("phone", unique = true),
        Index("role"),
        Index("created_at")
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "account")
    val account : String,

    @ColumnInfo(name = "name")
    val name : String?,

    @ColumnInfo(name = "password_hash")
    val passwordHash : String,

    @ColumnInfo(name = "role")
    val role : String = "default",

    @ColumnInfo(name = "phone")
    var phone: String ?= null,

    @ColumnInfo(name = "address")
    val address: String ?= null,

    @ColumnInfo(name = "created_at")
    val createdAt : Long = System.currentTimeMillis()
)
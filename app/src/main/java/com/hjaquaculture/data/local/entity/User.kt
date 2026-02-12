package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 用户
 * @param id 主键
 * @param account 账号
 * @param username 用户名
 * @param createdAt 创建时间
 * @param role 角色
 * @param passwordHash 密码的哈希值
 */
@Entity(
    tableName = "user",
    indices = [
        Index("account", unique = true,),
        Index("username", unique = true)
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "account")
    val account : String,

    @ColumnInfo(name = "username")
    val username : String? = "",

    @ColumnInfo(name = "password_hash")
    val passwordHash : String,

    @ColumnInfo(name = "role")
    val role : String = "user",

    @ColumnInfo(name = "created_at")
    val createdAt : Long = System.currentTimeMillis()
)
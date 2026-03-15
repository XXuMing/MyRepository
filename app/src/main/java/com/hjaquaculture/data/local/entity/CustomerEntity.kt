package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 客户
 * @param id 主键
 * @param name 客户名
 * @param nickName 昵称
 * @param phone 电话
 * @param passwordHash 密码的哈希值
 * @param address 地址
 * @param createdAt 创建时间
 */
@Entity(
    tableName = "customer",
    indices = [
        Index("name", unique = true),
        Index("nick_name", unique = true),
        Index("phone", unique = true),
        Index("created_at")
    ]
)
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "name")
    val name : String,

    @ColumnInfo(name = "nick_name")
    var nickName : String ?= null,

    @ColumnInfo(name = "phone")
    var phone: String ?= null,

    @ColumnInfo(name = "password_hash")
    val passwordHash : String,

    @ColumnInfo(name = "address")
    val address : String ?= null,

    @ColumnInfo(name = "created_at")
    val createdAt : Long = System.currentTimeMillis(),
)
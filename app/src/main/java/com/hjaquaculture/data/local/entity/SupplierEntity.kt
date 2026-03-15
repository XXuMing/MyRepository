package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 供应商
 * @param id 主键
 * @param name 供应商名
 * @param nickName 昵称
 * @param phone 电话
 * @param address 地址
 * @param createdAt 创建时间
 */
@Entity(
    tableName = "supplier",
    indices =[
        Index("name", unique = true),
        Index("nick_name", unique = true),
        Index("phone", unique = true),
        Index("created_at")
    ]
)
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone")
    val phone: String ?= null,

    @ColumnInfo(name = "nick_name")
    val nickName: String ?= null,

    @ColumnInfo(name = "address")
    val address: String ?= null,

    @ColumnInfo(name = "created_at")
    val createdAt : Long = System.currentTimeMillis()
)
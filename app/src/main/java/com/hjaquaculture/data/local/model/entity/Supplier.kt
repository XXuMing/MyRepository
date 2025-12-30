package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 供应商
 * @param id 主键
 * @param name 供应商名
 * @param phone 电话
 * @param nickname 昵称
 * @param address 地址
 * @param createdAt 创建时间
 */
@Entity(
    tableName = "supplier",
    indices =[
        Index("name", unique = true),
        Index("phone", unique = true)
    ]
)
data class Supplier(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "nickname")
    val nickname: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "created_at")
    val createdAt : Long = System.currentTimeMillis()
)
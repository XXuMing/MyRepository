package com.hjaquaculture.data.local.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 操作记录
 * @param id 主键
 * @param eventTime 事件时间
 * @param userId 用户ID
 * @param module 模块
 * @param description 描述
 */
@Entity(
    tableName = "operation_logs",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"]
    )],
    indices = [
        Index("user_id"),
        Index("module"),
        Index("eventTime")
    ]
//这里需要用到索引吗？
)
data class OperationLogs(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "eventTime")
    val eventTime  : Long = System.currentTimeMillis(),

    @ColumnInfo(name = "user_id")
    val userId : Long,

    @ColumnInfo(name = "module")
    val module : String,

    @ColumnInfo(name = "description")
    val description : String,
)
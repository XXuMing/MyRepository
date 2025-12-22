package com.hjaquaculture.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 操作记录
 */
@Entity(tableName = "operation_table")
data class OperationEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val operator : UserEntity,
    val time : String,
    val log : String
)
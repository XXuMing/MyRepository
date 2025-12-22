package com.hjaquaculture.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant


/**
 * 用户
 * @param
 */
@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val name : String,
    val createdAt : Instant,
    val password : String
)
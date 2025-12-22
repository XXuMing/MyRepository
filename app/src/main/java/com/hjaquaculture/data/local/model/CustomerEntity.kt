package com.hjaquaculture.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 客户
 */
@Entity(tableName = "customers_table")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val name : String,
    val password : String,
    var nikeName : String,
    var phoneNumber: Number,
    val createdAt : String,
    val address : String
)
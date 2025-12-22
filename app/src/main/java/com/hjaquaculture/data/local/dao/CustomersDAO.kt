package com.hjaquaculture.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hjaquaculture.data.local.model.CustomerEntity

@Dao
interface CustomersDAO {
    @Insert
    suspend fun insertUser(customers: CustomerEntity)

    @Query("SELECT * FROM customers_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<CustomerEntity>>

    @Query("DELETE FROM customers_table")
    suspend fun deleteAll()
}
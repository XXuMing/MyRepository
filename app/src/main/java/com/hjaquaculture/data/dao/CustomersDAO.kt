package com.hjaquaculture.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hjaquaculture.data.model.Customers

@Dao
interface CustomersDAO {
    @Insert
    suspend fun insertUser(customers: Customers)

    @Query("SELECT * FROM customers_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<Customers>>

    @Query("DELETE FROM customers_table")
    suspend fun deleteAll()
}
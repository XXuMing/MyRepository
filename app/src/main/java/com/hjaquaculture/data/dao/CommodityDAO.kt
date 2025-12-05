package com.hjaquaculture.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hjaquaculture.data.model.Commodity

@Dao
interface CommodityDAO{
    @Insert
    suspend fun insertUser(commodity: Commodity)

    @Query("SELECT * FROM commodity_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<Commodity>>

    @Query("DELETE FROM commodity_table")
    suspend fun deleteAll()
}
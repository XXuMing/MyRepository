package com.hjaquaculture.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hjaquaculture.data.local.model.CommodityEntity

@Dao
interface CommodityDAO{
    @Insert
    suspend fun insertUser(commodity: CommodityEntity)

    @Query("SELECT * FROM commodity_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<CommodityEntity>>

    @Query("DELETE FROM commodity_table")
    suspend fun deleteAll()
}
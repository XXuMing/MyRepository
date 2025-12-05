package com.hjaquaculture.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hjaquaculture.data.model.CommodityDetails

@Dao
interface CommodityDetailsDAO {    @Insert
suspend fun insertUser(commodity: CommodityDetails)

    @Query("SELECT * FROM commodityDetails_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<CommodityDetails>>

    @Query("DELETE FROM commodityDetails_table")
    suspend fun deleteAll()

}
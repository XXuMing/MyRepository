package com.hjaquaculture.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hjaquaculture.data.local.model.CommodityDetailsEntity

@Dao
interface CommodityDetailsDAO {    @Insert
suspend fun insertUser(commodity: CommodityDetailsEntity)

    @Query("SELECT * FROM commodityDetails_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<CommodityDetailsEntity>>

    @Query("DELETE FROM commodityDetails_table")
    suspend fun deleteAll()

}
package com.hjaquaculture.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hjaquaculture.data.local.model.OperationEntity

@Dao
interface OperationDAO {

    @Insert
    suspend fun insertUser(operation: OperationEntity)

    @Query("SELECT * FROM operation_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<OperationEntity>>

    @Query("DELETE FROM operation_table")
    suspend fun deleteAll()
}
package com.hjaquaculture.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hjaquaculture.data.model.Operation

@Dao
interface OperationDAO {

    @Insert
    suspend fun insertUser(operation: Operation)

    @Query("SELECT * FROM operation_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<Operation>>

    @Query("DELETE FROM operation_table")
    suspend fun deleteAll()
}
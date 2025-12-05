package com.hjaquaculture.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hjaquaculture.data.model.User


@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User) // 插入单个用户

    // 使用 @Query 执行自定义 SQL
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<User>> // 获取所有用户，使用 LiveData 观察数据变化

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()
}
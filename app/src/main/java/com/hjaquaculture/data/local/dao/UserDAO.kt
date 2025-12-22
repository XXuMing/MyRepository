package com.hjaquaculture.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    // 使用 @Query 执行自定义 SQL
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<UserEntity>> // 获取所有用户，使用 LiveData 观察数据变化

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_table WHERE name = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    // --- 插入 (Create) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long // 返回新插入行的 Row ID

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(users: List<UserEntity>): List<Long>

    // --- 查询 (Read) ---

    // 1. 一次性查询（用于逻辑处理）
    @Query("SELECT * FROM user_table WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    // 2. 响应式查询（用于 UI 自动刷新）
    @Query("SELECT * FROM user_table ORDER BY name ASC")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    // 3. 简单的条件查询
    //@Query("SELECT * FROM user_table WHERE age > :minAge")
    //suspend fun getUsersOlderThan(minAge: Int): List<User>

    // 只需要返回 PagingSource，Room 会自动帮你处理分页 SQL
    //@Query("SELECT * FROM user_table ORDER BY name ASC")
    //fun getAllUsersPaged(): PagingSource<Int, User>

    // --- 更新 (Update) ---

    @Update
    suspend fun updateUser(user: UserEntity): Int // 返回受影响的行数

    // --- 删除 (Delete) ---

    @Delete
    suspend fun deleteUser(user: UserEntity): Int

    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()
}
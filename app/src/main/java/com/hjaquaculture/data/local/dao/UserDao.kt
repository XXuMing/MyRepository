package com.hjaquaculture.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hjaquaculture.data.local.model.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * 用户数据访问对象 (DAO)
 *
 * 提供了与 'users' 表进行交互的所有方法。
 * 推荐使用 Flow 进行响应式数据查询。
 */
@Dao
interface UserDao {

    // --- 插入 (Create) ---

    /**
     * 插入单个用户。如果已存在，则替换旧数据。
     * @param user 要插入的用户对象
     * @return 返回新插入行的 Row ID，可用于判断是否插入成功
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    /**
     * 批量插入用户列表。如果已存在，则忽略冲突。
     * @param users 用户对象列表
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<User>)


    // --- 更新 (Update) ---

    /**
     * 更新单个用户。
     * @param user 要更新的用户对象
     * @return 返回受影响的行数，通常为 1
     */
    @Update
    suspend fun update(user: User): Int


    // --- 删除 (Delete) ---

    /**
     * 删除单个用户。
     * @param user 要删除的用户对象
     * @return 返回受影响的行数，通常为 1
     */
    @Delete
    suspend fun delete(user: User): Int

    /**
     * 删除表中的所有用户。
     */
    @Query("DELETE FROM user")
    suspend fun deleteAll()


    // --- 查询 (Read) ---

    /**
     * 【推荐】响应式查询：获取所有用户，并按用户名升序排序。
     * 返回一个 Flow，当数据变化时，它会自动发射最新的用户列表。非常适合在 UI 层观察。
     * @return 包含所有用户列表的 Flow
     */
    @Query("SELECT * FROM user ORDER BY username ASC")
    fun getAllUsers(): Flow<List<User>>

    /**
     * 一次性查询：根据用户 ID 获取单个用户。
     * 适用于在业务逻辑中进行一次性数据读取。
     * @param userId 用户的 ID
     * @return 返回用户对象，如果不存在则为 null
     */
    @Query("SELECT * FROM User WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    /**
     * 一次性查询：根据账户名获取单个用户。
     * @param account 用户的账号名称
     * @return 返回用户对象，如果不存在则为 null
     */
    @Query("SELECT * FROM User WHERE account = :account")
    suspend fun getUserByAccount(account: String): User?

    /**
     * 一次性查询：根据用户名获取单个用户。
     * @param username 用户的名称
     * @return 返回用户对象，如果不存在则为 null
     */
    @Query("SELECT * FROM User WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

}

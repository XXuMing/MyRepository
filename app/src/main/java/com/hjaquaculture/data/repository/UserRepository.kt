package com.hjaquaculture.data.repository

import com.hjaquaculture.data.local.dao.UserDao
import com.hjaquaculture.data.local.model.UserEntity
import jakarta.inject.Inject
import java.time.Instant

class UserRepository @Inject constructor(
    private val userDao: UserDao // 通过 Hilt 注入 DAO
) {
    // 定义返回结果的密封类
    sealed class RegisterResult {
        object Success : RegisterResult()
        object AlreadyExists : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    /**
     * 注册用户
     * @param username 用户名
     * @param passwordHash 密码的哈希值
     */
    suspend fun registerUser(username: String, passwordHash: String): RegisterResult {
        return try {
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                RegisterResult.AlreadyExists
            } else {
                userDao.insertUser(UserEntity(name = username, password = passwordHash, createdAt = Instant.now()))
                RegisterResult.Success
            }
        } catch (e: Exception) {
            RegisterResult.Error(e.localizedMessage ?: "未知错误")
        }
    }
}
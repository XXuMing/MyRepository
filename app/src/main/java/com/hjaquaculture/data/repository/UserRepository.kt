package com.hjaquaculture.data.repository

import com.hjaquaculture.data.local.dao.UserDao
import com.hjaquaculture.data.local.model.entity.User
import jakarta.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    // 定义返回结果的密封类
    sealed class RegisterResult {
        object Success : RegisterResult()
        object AlreadyExists : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    /**
     * 注册用户
     * @param account 用户名
     * @param passwordHash 密码的哈希值
     */
    suspend fun registerUser(account: String, passwordHash: String): RegisterResult {
        return try {
            val existingUser = userDao.getUserByAccount(account)
            if (existingUser != null) {
                RegisterResult.AlreadyExists
            } else {
                userDao.insert(
                    User(
                        account = account,
                        username = account,
                        passwordHash = passwordHash
                    )
                )
                RegisterResult.Success
            }
        } catch (e: Exception) {
            RegisterResult.Error(e.localizedMessage ?: "未知错误")
        }
    }

    /**
     *
     */
    suspend fun login(username:String,passwordHash: String){

    }
}
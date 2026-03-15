package com.hjaquaculture.data.local.repository

import com.hjaquaculture.data.local.dao.UserDao
import com.hjaquaculture.data.local.entity.UserEntity
import com.hjaquaculture.feature.home.RegisterResult
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class UserRepository @Inject constructor(
    private val userDao : UserDao
) {

    suspend fun login(username: String, passwordHash: String) {
        TODO("Not yet implemented")
    }

    suspend fun registerUser(account: String, passwordHash: String): RegisterResult {
        return try {
            val existingUser = userDao.getUserByAccount(account)
            if (existingUser != null) {
                RegisterResult.AlreadyExists
            } else {
                userDao.insert(
                    UserEntity(
                        account = account,
                        name = account,
                        passwordHash = passwordHash
                    )
                )
                RegisterResult.Success
            }
        } catch (e: Exception) {
            RegisterResult.Error(e.localizedMessage ?: "未知错误")
        }
    }
}
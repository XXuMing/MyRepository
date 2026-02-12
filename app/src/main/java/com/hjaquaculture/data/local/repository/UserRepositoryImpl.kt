package com.hjaquaculture.data.local.repository

import com.hjaquaculture.data.local.dao.UserDao
import com.hjaquaculture.data.local.entity.User
import com.hjaquaculture.domain.repository.RegisterResult
import com.hjaquaculture.domain.repository.UserRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao : UserDao
): UserRepository {

    override suspend fun login(username: String, passwordHash: String) {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(account: String, passwordHash: String): RegisterResult {
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
}
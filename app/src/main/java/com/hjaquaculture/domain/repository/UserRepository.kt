package com.hjaquaculture.domain.repository

import com.hjaquaculture.data.local.dao.UserDao
import com.hjaquaculture.data.local.entity.User
import jakarta.inject.Inject
import jakarta.inject.Singleton

/**
 * 用户仓库接口
 */
interface UserRepository{
    /**
     * 注册用户
     * @param account 账号
     * @param passwordHash 密码哈希值
     * @return 注册结果
     */
    suspend fun registerUser(account: String, passwordHash: String): RegisterResult

    /**
     * 登录
     * @param username 用户名
     * @param passwordHash 密码哈希值
     */
    suspend fun login(username:String,passwordHash: String)

}


/**
 * 注册结果密封类
 */
sealed class RegisterResult {
    /**
     * 注册成功
     */
    object Success : RegisterResult()

    /**
     * 账号已存在
     */
    object AlreadyExists : RegisterResult()

    /**
     * 注册失败
     */
    data class Error(val message: String) : RegisterResult()
}
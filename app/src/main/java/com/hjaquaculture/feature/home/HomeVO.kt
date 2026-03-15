package com.hjaquaculture.feature.home


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
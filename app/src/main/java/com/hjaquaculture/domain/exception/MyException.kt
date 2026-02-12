package com.hjaquaculture.domain.exception

// 定义一个基础异常类
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    // 数据库相关错误
    class DatabaseException(message: String, val errorCode: Int) : AppException(message)

    // 网络相关错误
    class NetworkException(val code: Int, message: String) : AppException("网络错误 [$code]: $message")

    // 业务逻辑错误
    class ValidationException(message: String) : AppException(message)
}

/*
使用方式

class DataConstraintException(
    val fieldName: String, // 发生冲突的字段，如 "email"
    override val message: String,
    cause: Throwable? = null
) : Exception(message, cause)

// 在代码中这样抛出
fun handleSqliteError(e: SQLiteConstraintException) {
    val msg = e.message ?: ""
    when {
        msg.contains("User.email") ->
            throw DataConstraintException("email", "该邮箱已被注册", e)
        msg.contains("User.id") ->
            throw DataConstraintException("id", "主键冲突", e)
        else -> throw e
    }
}
*/
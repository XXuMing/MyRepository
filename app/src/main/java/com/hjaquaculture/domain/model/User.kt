package com.hjaquaculture.domain.model

/**
 * Domain 层用户对象
 * 移除了数据库注解，专注于业务属性
 */
data class User(
    val id: Long,
    val account: String,
    val name: String,
    val phone: String,
    val role: UserRole, // 使用枚举增强类型安全
    val address: String,
    val createdAt: Long
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 判断用户是否具有管理员权限
     */
    val isAdmin: Boolean
        get() = role == UserRole.ADMIN

    /**
     * 校验电话号码格式是否合法
     */
    fun isPhoneValid(): Boolean {
        return phone.isNotBlank() && phone.matches(Regex("^1[3-9]\\d{9}$"))
    }

    /**
     * 获取脱敏后的电话号码 (例如: 138****1234)
     */
    fun getMaskedPhone(): String {
        return if (isPhoneValid()) {
            phone.replaceRange(3, 7, "****")
        } else "Invalid Phone"
    }
}

/**
 * 角色枚举，避免使用硬编码字符串
 */
enum class UserRole(val value: String) {
    ADMIN("admin"),
    USER("user"),
    DEFAULT("default");

    companion object {
        fun fromString(value: String?) = entries.find { it.value == value } ?: DEFAULT
    }
}
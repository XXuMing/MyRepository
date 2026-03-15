package com.hjaquaculture.domain.model

/**
 * 客户领域模型
 */
data class Customer(
    val id: Long,
    val name: String,
    val nickName: String, // 统一命名为 nickName，纠正 Entity 中的拼写
    val phone: String,
    val address: String,
    val createdAt: Long
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 获取优先展示名称
     * 逻辑：如果有昵称则显示昵称，否则显示真实姓名
     */
    val displayName: String
        get() = nickName.ifBlank { name }

    /**
     * 手机号脱敏
     * 逻辑：中间四位隐藏
     */
    val maskedPhone: String
        get() = if (phone.length >= 11) {
            phone.replaceRange(3, 7, "****")
        } else {
            phone
        }

    /**
     * 获取注册时长（天）
     */
    fun getRegistrationDays(): Long {
        val diff = System.currentTimeMillis() - createdAt
        return diff / (1000 * 60 * 60 * 24)
    }
}
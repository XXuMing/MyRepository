package com.hjaquaculture.domain.model

/**
 * 供应商领域模型
 */
data class Supplier(
    val id: Long,
    val name: String,
    val nickName: String,
    val phone: String,
    val address: String,
    val createdAt: Long
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 优先显示的名称
     * 逻辑：如果存在昵称则显示昵称，否则显示正式名称
     */
    val displayName: String
        get() = nickName.ifBlank { name }

    /**
     * 电话脱敏处理
     */
    val maskedPhone: String
        get() = if (phone.length >= 11) {
            phone.replaceRange(3, 7, "****")
        } else {
            phone.ifBlank { "无联系方式" }
        }

    /**
     * 合作时长（天）
     */
    val cooperationDays: Long
        get() = (System.currentTimeMillis() - createdAt) / (1000 * 60 * 60 * 24)
}
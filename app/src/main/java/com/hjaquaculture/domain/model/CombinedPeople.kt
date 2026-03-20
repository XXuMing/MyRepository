package com.hjaquaculture.domain.model

import com.hjaquaculture.common.base.PartySymbol

/**
 * 合并人员概要领域模型
 */
data class CombinedPeople(
    val symbol: PartySymbol,
    val id: Long,
    val name: String,
    val phone: String,
    val account: String,
    val nickName: String,
    val role: String,
    val address: String,
    val createdAt: Long
) {
    // --- 业务规则 (Business Rules) ---

    /**
     * 统一称呼逻辑
     * 逻辑：有昵称用昵称（针对客户），否则用真实姓名
     */
    val displayName: String
        get() = nickName.ifBlank { name }

    /**
     * 身份副标题
     * 逻辑：员工显示角色，客户显示昵称，供应商显示地址前缀
     */
    val identitySubtitle: String
        get() = when (symbol) {
            PartySymbol.OPERATOR -> role.ifBlank { "普通员工" }
            PartySymbol.CUSTOMER -> if (nickName.isNotBlank()) "昵称: $nickName" else "正式客户"
            PartySymbol.SUPPLIER -> "供应商"
        }

    /**
     * 是否有联系方式
     */
    val hasContact: Boolean get() = phone.isNotBlank()
}
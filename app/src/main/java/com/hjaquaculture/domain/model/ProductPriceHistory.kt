package com.hjaquaculture.domain.model

/**
 * 商品调价历史领域模型
 */
data class PriceHistory(
    val id: Long,
    val productId: Long,
    val operatorId: Long,
    val price: Long,
    val changedAt: Long,
    val remark: String? = null
)
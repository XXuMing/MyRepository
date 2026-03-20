package com.hjaquaculture.domain.model

import com.hjaquaculture.common.base.StockChangeType

/**
 * 库存变动流水领域模型
 */
data class InventoryLog(
    val id: Long,
    val productId: Long,
    val changeType: StockChangeType, // 使用之前定义的枚举
    val amount: Int,                 // 变动数量（正数为入，负数为出）
    val balanceSnapshot: Int,       // 变动后的库存快照
    val refOrderId: Long?,
    val operatorId: Long,
    val createdAt: Long,
    val remark: String,
) {
    /**
     * 获取变动的文本描述
     * 示例："+10" 或 "-5"
     */
    val amountText: String
        get() = if (amount > 0) "+$amount" else amount.toString()

    /**
     * 简单的日期格式化（逻辑解释：在领域层提供基础格式，方便 UI 直接使用）
     */
    val formattedDate: String
        get() = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
            .format(java.util.Date(createdAt))
}
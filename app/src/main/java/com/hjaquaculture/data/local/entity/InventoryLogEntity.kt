package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/**
 * 库存变动日志
 */
/*
@Entity(
    tableName = "inventory_log",
    indices = [
        Index("product_id"),
        Index("ref_order_sn"), // 关联单据编号，方便追溯
        Index("created_at")
    ]
)

 */
data class InventoryLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    // 变动类型：1:采购入库, 2:销售出库, 3:调价折损, 4:盘点修正
    @ColumnInfo(name = "change_type")
    val changeType: Int,

    // 变动数量：入库为正（+10），出库为负（-5）
    @ColumnInfo(name = "amount")
    val amount: Int,

    // 变动后的库存快照（审计用）
    @ColumnInfo(name = "balance_snapshot")
    val balanceSnapshot: Int,

    @ColumnInfo(name = "ref_order_sn")
    val refOrderSn: String? = null,

    @ColumnInfo(name = "remark")
    val remark: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
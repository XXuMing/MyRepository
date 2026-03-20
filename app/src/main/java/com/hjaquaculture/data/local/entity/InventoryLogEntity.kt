package com.hjaquaculture.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hjaquaculture.common.base.StockChangeType

/**
 * 库存变动日志
 * @param id 主键
 * @param productId 商品ID
 * @param changeType 变动类型
 * @param amount 变动数量
 * @param balanceSnapshot 变动后的库存快照
 * @param refOrderId 关联订单ID
 * @param operatorId 操作员ID
 * @param createdAt 创建时间
 * @param remark 备注
 */

@Entity(
    tableName = "inventory_log",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.RESTRICT,// 有库存日志的商品不允许删除
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["operator_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("product_id"),
        Index("change_type"),
        Index("operator_id"),
        Index("created_at"),
    ]
)
data class InventoryLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "change_type")
    val changeType: StockChangeType,

    @ColumnInfo(name = "amount")
    val amount: Int,

    @ColumnInfo(name = "balance_snapshot")
    val balanceSnapshot: Int,

    @ColumnInfo(name = "ref_order_id")
    val refOrderId: Long?,

    @ColumnInfo(name = "operator_id")
    val operatorId: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "remark")
    val remark: String? = null,
)

// 对于你的系统，refOrderId + changeType 是最平衡的方案。
// 你已经有了 changeType 枚举，refOrderId 配合它就能唯一确定关联来源，不需要字符串解析，也不会字段膨胀。数据库级外键约束的缺失可以用应用层的 Repository 事务来补偿：
// 在 Repository 里保证：
// 写入 inventory_log 之前，先验证 refOrderId 对应的订单存在
// 放在同一个事务里，保证原子性
/*
database.withTransaction {
    val orderId = saleOrderDao.insert(order)
    inventoryLogDao.insert(
        InventoryLogEntity(
            changeType = StockChangeType.SALE_OUT,
            refOrderId = orderId,
            ...
    )
    )
}
 */
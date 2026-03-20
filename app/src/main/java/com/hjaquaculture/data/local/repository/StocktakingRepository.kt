package com.hjaquaculture.data.local.repository

import androidx.room.withTransaction
import com.hjaquaculture.common.base.StockChangeType
import com.hjaquaculture.common.base.StocktakingStatus
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.InventoryDao
import com.hjaquaculture.data.local.dao.InventoryLogDao
import com.hjaquaculture.data.local.dao.StocktakingDao
import com.hjaquaculture.data.local.dao.StocktakingDetailDao
import com.hjaquaculture.data.local.entity.InventoryLogEntity
import com.hjaquaculture.data.local.entity.StocktakingDetailEntity
import com.hjaquaculture.data.local.entity.StocktakingEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class StocktakingRepository @Inject constructor(
    private val database: LocalDatabase,
    private val inventoryDao: InventoryDao,
    private val inventoryLogDao: InventoryLogDao,
    private val stDao: StocktakingDao,
    private val stDetailDao: StocktakingDetailDao
) {

    /**
     * 开始盘点
     */
    suspend fun startStocktaking(operatorId: Long): Long {
        return database.withTransaction {

            // 1. 检查是否已有进行中的盘点
            val inProgressCount = stDao.getInProgressCount()
            if (inProgressCount > 0) {
                throw IllegalStateException("已有进行中的盘点，请先完成或取消")
            }

            // 2. 创建盘点单
            val stocktakingId = stDao.insert(
                StocktakingEntity(operatorId = operatorId)
            )

            // 3. 查询所有在售商品的当前库存
            val inventories = inventoryDao.getAllSnapshot()

            // 4. 批量生成盘点明细，快照当前库存数量
            val items = inventories.map { inventory ->
                StocktakingDetailEntity(
                    stocktakingId = stocktakingId,
                    productId = inventory.productId,
                    systemAmount = inventory.amount,  // 快照当前库存
                    actualAmount = null               // 等待录入
                )
            }
            stDetailDao.insertAll(items)

            stocktakingId
        }
    }

    /**
     * 录入盘点明细
     */
    suspend fun completeStocktaking(stocktakingId: Long, operatorId: Long) {
        database.withTransaction {

            // 1. 查询所有有差异的明细
            val differentItems = stDetailDao.getDifferentItems(stocktakingId)

            // 2. 逐一调整库存
            differentItems.forEach { item ->
                val difference = item.difference ?: return@forEach

                // 2a. 更新库存表
                inventoryDao.adjustAmount(item.productId, difference)

                // 2b. 获取最新快照
                val snapshot = inventoryLogDao.getLatestSnapshot(item.productId) ?: 0

                // 2c. 写入库存流水
                inventoryLogDao.insert(
                    InventoryLogEntity(
                        productId = item.productId,
                        changeType = StockChangeType.ADJUST,
                        amount = difference,
                        balanceSnapshot = snapshot,
                        operatorId = operatorId,
                        remark = if (difference > 0) "盘盈" else "盘亏"
                    )
                )
            }

            // 3. 更新盘点单状态
            stDao.updateStatus(
                id = stocktakingId,
                status = StocktakingStatus.COMPLETED,
                completedAt = System.currentTimeMillis()
            )
        }
    }

    /**
     * 取消盘点
     */
    suspend fun cancelStocktaking(stocktakingId: Long) {
        // 取消不需要调整库存，只改状态即可
        // StocktakingItemEntity 会因为 CASCADE 自动删除
        stDao.updateStatus(
            id = stocktakingId,
            status = StocktakingStatus.CANCELLED,
            completedAt = null
        )
    }
    /*
    但这里有个问题需要确认：**取消盘点时，明细数据要不要保留？**

    保留明细：→ 外键去掉 CASCADE，改为 RESTRICT
    优点：可以查看历史上取消的盘点记录，知道当时盘到哪里了
    缺点：数据量增大

    不保留明细：→ 保持 CASCADE(建议)
    优点：数据库整洁
    缺点：取消的盘点记录没有参考价值
     */
}
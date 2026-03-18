package com.hjaquaculture.data.local.repository

import androidx.room.withTransaction
import com.hjaquaculture.common.utils.OrderManager
import com.hjaquaculture.common.utils.SnPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.PurchaseOrderDao
import com.hjaquaculture.data.local.dao.PurchaseOrderItemDao
import com.hjaquaculture.data.local.entity.PurchaseOrderEntity
import com.hjaquaculture.data.local.entity.PurchaseOrderItemEntity
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Singleton
class PurchaseOrderRepository @Inject constructor(
    private val database: LocalDatabase,
    private val dao: PurchaseOrderDao,
    private val itemDao: PurchaseOrderItemDao,
    private val orderManager: OrderManager
) {

    suspend fun addPurchaseOrder (purchaseOrderEntity : PurchaseOrderEntity): Long{
        return database.withTransaction {
            val newId = dao.insert(purchaseOrderEntity)
            val sn = orderManager.generateSn(SnPrefix.PURCHASE_ORDER, newId)
            dao.updateSn(newId, sn)
            newId
        }
    }

    fun getItems(orderId: Long): Flow<List<PurchaseOrderItemEntity>> {
        return itemDao.getForPurchaseOrder(orderId)
    }

    /**
     * 检查数据库是否为空
     */
    suspend fun isDatabaseEmpty(): Boolean {
        // 只要主订单表没数据，我们就认为需要初始化
        return dao.getCount() == 0
    }
}
package com.hjaquaculture.data.local.repository

import androidx.room.withTransaction
import com.hjaquaculture.common.utils.OrderManager
import com.hjaquaculture.common.utils.OrderPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.PurchaseOrderDao
import com.hjaquaculture.data.local.entity.PurchaseOrder
import com.hjaquaculture.domain.repository.PurchaseOrderRepository
import jakarta.inject.Singleton
import javax.inject.Inject

@Singleton
class PurchaseOrderRepositoryImpl @Inject constructor(
    private val database: LocalDatabase,
    private val dao: PurchaseOrderDao,
    private val orderManager: OrderManager
) : PurchaseOrderRepository {

    override suspend fun addPurchaseOrder (purchaseOrder : PurchaseOrder): Long{
        return database.withTransaction {
            val newId = dao.insert(purchaseOrder)
            val sn = orderManager.generateSn(OrderPrefix.PURCHASE_ORDER, newId)
            dao.updateSn(newId, sn)
            newId
        }
    }

    /**
     * 检查数据库是否为空
     */
    suspend fun isDatabaseEmpty(): Boolean {
        // 只要主订单表没数据，我们就认为需要初始化
        return dao.getCount() == 0
    }
}
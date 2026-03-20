package com.hjaquaculture.data.local.repository

import androidx.room.withTransaction
import com.hjaquaculture.common.base.OrderManager
import com.hjaquaculture.common.base.SnPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.PurchasePaymentDao
import com.hjaquaculture.data.local.entity.PurchasePaymentEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class PurchasePaymentRepository @Inject constructor(
    private val database: LocalDatabase,
    private val dao: PurchasePaymentDao,
    private val orderManager: OrderManager
) {

    suspend fun addPurchasePayment(purchasePaymentEntity: PurchasePaymentEntity): Long{
        return database.withTransaction {
            val newId = dao.insert(purchasePaymentEntity)
            val sn = orderManager.generateSn(SnPrefix.PURCHASE_PAYMENT, newId)
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
package com.hjaquaculture.data.local.repository

import androidx.room.withTransaction
import com.hjaquaculture.common.utils.OrderManager
import com.hjaquaculture.common.utils.OrderPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.SalePaymentDao
import com.hjaquaculture.data.local.entity.SalePayment
import com.hjaquaculture.domain.repository.SalePaymentRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class SalePaymentRepositoryImpl @Inject constructor(
    private val database: LocalDatabase,
    private val dao: SalePaymentDao,
    private val orderManager: OrderManager
) : SalePaymentRepository {

    override suspend fun addSalePayment(salePayment: SalePayment) : Long{
        return database.withTransaction {
            val newId = dao.insert(salePayment)
            val sn = orderManager.generateSn(OrderPrefix.SALE_PAYMENT, newId)
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
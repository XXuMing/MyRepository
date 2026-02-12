package com.hjaquaculture.data.local.repository

import androidx.room.withTransaction
import com.hjaquaculture.common.utils.OrderManager
import com.hjaquaculture.common.utils.OrderPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.SaleOrderDao
import com.hjaquaculture.data.local.entity.SaleOrder
import com.hjaquaculture.domain.repository.SaleOrderRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class SaleOrderRepositoryImpl @Inject constructor(
    private val database: LocalDatabase,
    private val dao: SaleOrderDao,
    private val orderManager: OrderManager
) : SaleOrderRepository {

    override suspend fun addSaleOrder (saleOrder : SaleOrder): Long {
        // 使用数据库事务保证原子性
        return database.withTransaction {
            // 默认sn = null，先插入新数据返回id
            val newId = dao.insert(saleOrder)
            // 通过id生成sn
            val sn = orderManager.generateSn(OrderPrefix.SALE_ORDER, newId)
            // 单独更新该id的sn
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
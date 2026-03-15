package com.hjaquaculture.data.local.repository

import androidx.room.withTransaction
import com.hjaquaculture.common.utils.OrderManager
import com.hjaquaculture.common.utils.OrderPrefix
import com.hjaquaculture.data.local.LocalDatabase
import com.hjaquaculture.data.local.dao.SaleOrderDao
import com.hjaquaculture.data.local.dao.SaleOrderItemDao
import com.hjaquaculture.data.local.entity.SaleOrderEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class SaleOrderRepository @Inject constructor(
    private val database: LocalDatabase,
    private val dao: SaleOrderDao,
    private val itemDao: SaleOrderItemDao,
    private val orderManager: OrderManager
) {

    suspend fun add (saleOrderEntity : SaleOrderEntity): Long {
        // 使用数据库事务保证原子性
        return database.withTransaction {
            // 默认sn = null，先插入新数据返回id
            val newId = dao.insert(saleOrderEntity)
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
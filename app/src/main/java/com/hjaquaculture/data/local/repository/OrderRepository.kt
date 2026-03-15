package com.hjaquaculture.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hjaquaculture.common.utils.OrderSymbol
import com.hjaquaculture.data.local.dao.CombinedOrderDao
import com.hjaquaculture.data.local.dao.PurchaseOrderDao
import com.hjaquaculture.data.local.dao.PurchaseOrderItemDao
import com.hjaquaculture.data.local.dao.SaleOrderDao
import com.hjaquaculture.data.local.dao.SaleOrderItemDao
import com.hjaquaculture.feature.order.OrderItemVO
import com.hjaquaculture.feature.order.OrderVO
import com.hjaquaculture.feature.order.toVO
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class OrderRepository @Inject constructor(
    private val combinedDao: CombinedOrderDao,
    private val soDao: SaleOrderDao,
    private val poDao: PurchaseOrderDao,
    private val purItemDao: PurchaseOrderItemDao,
    private val saleItemDao: SaleOrderItemDao
) {
    /**
     * 获取所有订单的组合视图
     */
    fun getCombinedOrders(query: String, symbol: String?, type: String?, status:String?)
    : Flow<PagingData<OrderVO>>
    {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { combinedDao.getPagingSource(query, symbol, type, status)}
        ).flow.map{ pagingData ->
            pagingData.map{ view ->
                view.toVO()
            }
        }
    }

    fun getOrderItems(symbol: OrderSymbol, orderId: Long): Flow<List<OrderItemVO>> {
        return when (symbol.dbValue) {
            OrderSymbol.SALE.dbValue -> saleItemDao.getForSaleOrder(orderId).map{
                it.map { item -> item.toVO() }
            }
            OrderSymbol.PURCHASE.dbValue -> purItemDao.getForPurchaseOrder(orderId).map{
                it.map { item -> item.toVO() }
            }
            else -> throw IllegalArgumentException("Invalid symbol: $symbol")
        }
    }

}
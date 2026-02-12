package com.hjaquaculture.domain.repository

import com.hjaquaculture.data.local.entity.SaleOrder
import jakarta.inject.Singleton
@Singleton
interface SaleOrderRepository {
    suspend fun addSaleOrder (saleOrder : SaleOrder): Long
}
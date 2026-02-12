package com.hjaquaculture.domain.repository

import com.hjaquaculture.data.local.entity.PurchaseOrder

interface PurchaseOrderRepository {
    suspend fun addPurchaseOrder (purchaseOrder : PurchaseOrder): Long
}
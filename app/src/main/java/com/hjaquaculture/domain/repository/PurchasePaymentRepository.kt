package com.hjaquaculture.domain.repository

import com.hjaquaculture.data.local.entity.PurchasePayment

interface PurchasePaymentRepository{
    suspend fun addPurchasePayment(purchasePayment: PurchasePayment): Long
}
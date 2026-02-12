package com.hjaquaculture.domain.repository

import com.hjaquaculture.data.local.entity.SalePayment
import jakarta.inject.Singleton

@Singleton
interface SalePaymentRepository {
    suspend fun addSalePayment(salePayment: SalePayment) : Long
}
package com.hjaquaculture.data.local.repository

import com.hjaquaculture.data.local.dao.ProductPriceHistoryDao
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class ProductPriceHistoryRepository @Inject constructor(
    private val productPriceHistoryDao: ProductPriceHistoryDao
){

}
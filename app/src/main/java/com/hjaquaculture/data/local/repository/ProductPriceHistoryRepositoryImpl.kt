package com.hjaquaculture.data.local.repository

import com.hjaquaculture.data.local.dao.ProductPriceHistoryDao
import com.hjaquaculture.domain.repository.ProductPriceHistoryRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class ProductPriceHistoryRepositoryImpl @Inject constructor(
    private val productPriceHistoryDao: ProductPriceHistoryDao
) : ProductPriceHistoryRepository {

}
package com.hjaquaculture.domain.usecase

import com.hjaquaculture.domain.repository.ProductPriceHistoryRepository
import jakarta.inject.Inject

class ProductPriceHistoryUseCase (
    private val repository: ProductPriceHistoryRepository
) {

}
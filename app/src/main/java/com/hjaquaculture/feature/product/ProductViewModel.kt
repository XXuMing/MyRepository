package com.hjaquaculture.feature.product

import androidx.lifecycle.ViewModel
import com.hjaquaculture.data.local.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

}
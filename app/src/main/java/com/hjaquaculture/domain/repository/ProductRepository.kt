package com.hjaquaculture.domain.repository

import androidx.paging.PagingData
import com.hjaquaculture.data.local.entity.Product
import com.hjaquaculture.data.local.entity.ProductCategory
import com.hjaquaculture.data.local.entity.ProductWithCategory
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun addProduct(product: Product): Long

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(productId: Long)

    suspend fun getProductById(productId: Long): Product

    suspend fun getProductByName(productName: String): Flow<List<Product>>

    suspend fun getProductsByCategoryId(categoryId: Long): List<Product>

    suspend fun getAllProducts(): Flow<List<Product>>

    // -----
    suspend fun addCategory(category: ProductCategory): Long

    suspend fun updateCategory(category: ProductCategory)
    suspend fun updateCategory(id: Long, newName: String)

    suspend fun deleteCategory(categoryId: Long)

    suspend fun getCategoryById(categoryId: Long): ProductCategory

    fun getCategoryByName(categoryName: String): Flow<List<ProductCategory>>

    fun getAllCategories(): Flow<List<ProductCategory>>

    // ---  ---

    fun getCategoryWithProducts(): Flow<Map<ProductCategory, List<Product>>>

    suspend fun updateCategoriesOrder(categories: List<ProductCategory>)

}
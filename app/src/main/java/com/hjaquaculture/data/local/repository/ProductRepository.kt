package com.hjaquaculture.data.local.repository

import com.hjaquaculture.data.local.dao.ProductDao
import com.hjaquaculture.data.local.dao.ProductVarietyDao
import com.hjaquaculture.data.local.entity.ProductEntity
import com.hjaquaculture.data.local.entity.ProductVarietyEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val categoryDao: ProductVarietyDao
){

    // --- 商品 ---

    suspend fun addProduct(product: ProductEntity):Long {
        return productDao.insert(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        productDao.update(product)
    }

    suspend fun deleteProduct(productId: Long) {
        productDao.deleteById(productId)
    }

    suspend fun getProductById(productId: Long): ProductEntity {
        return productDao.getById(productId)
    }

    fun getProductByName(productName: String): Flow<List<ProductEntity>> {
        return productDao.getByName(productName)
    }

    suspend fun getProductsByCategoryId(categoryId: Long): List<ProductEntity> {
        return productDao.getByCategoryId(categoryId)
    }

    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAll()
    }

    // --- 分类 ---

    suspend fun addCategory(category: ProductVarietyEntity): Long {
        return categoryDao.insert(category)
    }

    suspend fun updateCategory(category: ProductVarietyEntity) {
        categoryDao.update(category)
    }

    suspend fun updateCategory(id: Long, newName: String) {
        categoryDao.update(id, newName)
    }

    suspend fun deleteCategory(categoryId: Long) {
        categoryDao.deleteById(categoryId)
    }

    suspend fun getCategoryById(categoryId: Long): ProductVarietyEntity{
        return categoryDao.getById(categoryId)
    }

    fun getCategoryByName(categoryName: String): Flow<List<ProductVarietyEntity>> {
        return categoryDao.getByName(categoryName)
    }

    fun getAllCategories(): Flow<List<ProductVarietyEntity>> {
        return categoryDao.getAll()
    }

    // ---  ---
    fun getCategoryWithProducts(): Flow<Map<ProductVarietyEntity, List<ProductEntity>>> {
        return categoryDao.getAllCategoriesWithProducts()
    }

    suspend fun updateCategoriesOrder(categories: List<ProductVarietyEntity>) {
        val updates = categories.mapIndexed { index, category ->
            category.copy(sort = index) // 将索引作为新的优先级
        }
        categoryDao.updateCategories(updates) // 批量更新数据库
    }
}
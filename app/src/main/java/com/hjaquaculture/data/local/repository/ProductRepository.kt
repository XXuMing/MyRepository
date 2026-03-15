package com.hjaquaculture.data.local.repository

import com.hjaquaculture.data.local.dao.ProductCategoryDao
import com.hjaquaculture.data.local.dao.ProductDao
import com.hjaquaculture.data.local.entity.ProductCategoryEntity
import com.hjaquaculture.data.local.entity.ProductEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val categoryDao: ProductCategoryDao
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

    suspend fun addCategory(category: ProductCategoryEntity): Long {
        return categoryDao.insert(category)
    }

    suspend fun updateCategory(category: ProductCategoryEntity) {
        categoryDao.update(category)
    }

    suspend fun updateCategory(id: Long, newName: String) {
        categoryDao.update(id, newName)
    }

    suspend fun deleteCategory(categoryId: Long) {
        categoryDao.deleteById(categoryId)
    }

    suspend fun getCategoryById(categoryId: Long): ProductCategoryEntity{
        return categoryDao.getById(categoryId)
    }

    fun getCategoryByName(categoryName: String): Flow<List<ProductCategoryEntity>> {
        return categoryDao.getByName(categoryName)
    }

    fun getAllCategories(): Flow<List<ProductCategoryEntity>> {
        return categoryDao.getAll()
    }

    // ---  ---
    fun getCategoryWithProducts(): Flow<Map<ProductCategoryEntity, List<ProductEntity>>> {
        return categoryDao.getAllCategoriesWithProducts()
    }

    suspend fun updateCategoriesOrder(categories: List<ProductCategoryEntity>) {
        val updates = categories.mapIndexed { index, category ->
            category.copy(sort = index) // 将索引作为新的优先级
        }
        categoryDao.updateCategories(updates) // 批量更新数据库
    }
}
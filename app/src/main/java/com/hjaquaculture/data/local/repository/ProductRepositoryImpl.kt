package com.hjaquaculture.data.local.repository

import android.R.attr.name
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hjaquaculture.data.local.dao.ProductCategoryDao
import com.hjaquaculture.data.local.dao.ProductDao
import com.hjaquaculture.data.local.entity.Product
import com.hjaquaculture.data.local.entity.ProductCategory
import com.hjaquaculture.data.local.entity.ProductWithCategory
import com.hjaquaculture.domain.repository.ProductRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val categoryDao: ProductCategoryDao
) : ProductRepository {

    // --- 商品 ---

    override suspend fun addProduct(product: Product):Long {
        return productDao.insert(product)
    }

    override suspend fun updateProduct(product: Product) {
        productDao.update(product)
    }

    override suspend fun deleteProduct(productId: Long) {
        productDao.deleteById(productId)
    }

    override suspend fun getProductById(productId: Long): Product {
        return productDao.getById(productId)
    }

    override suspend fun getProductByName(productName: String): Flow<List<Product>> {
        return productDao.getByName(productName)
    }

    override suspend fun getProductsByCategoryId(categoryId: Long): List<Product> {
        return productDao.getByCategoryId(categoryId)
    }

    override suspend fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAll()
    }

    // --- 分类 ---

    override suspend fun addCategory(category: ProductCategory): Long {
        return categoryDao.insert(category)
    }

    override suspend fun updateCategory(category: ProductCategory) {
        categoryDao.update(category)
    }

    override suspend fun updateCategory(id: Long, newName: String) {
        categoryDao.update(id, newName)
    }

    override suspend fun deleteCategory(categoryId: Long) {
        categoryDao.deleteById(categoryId)
    }

    override suspend fun getCategoryById(categoryId: Long): ProductCategory{
        return categoryDao.getById(categoryId)
    }

    override  fun getCategoryByName(categoryName: String): Flow<List<ProductCategory>> {
        return categoryDao.getByName(categoryName)
    }

    override fun getAllCategories(): Flow<List<ProductCategory>> {
        return categoryDao.getAll()
    }

    // ---  ---
    override fun getCategoryWithProducts(): Flow<Map<ProductCategory, List<Product>>> {
        return categoryDao.getAllCategoriesWithProducts()
    }

    override suspend fun updateCategoriesOrder(categories: List<ProductCategory>) {
        val updates = categories.mapIndexed { index, category ->
            category.copy(sort = index) // 将索引作为新的优先级
        }
        categoryDao.updateCategories(updates) // 批量更新数据库
    }
}
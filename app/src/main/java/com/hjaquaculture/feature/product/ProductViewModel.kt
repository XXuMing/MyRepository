package com.hjaquaculture.feature.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjaquaculture.data.local.entity.Product
import com.hjaquaculture.data.local.entity.ProductCategory
import com.hjaquaculture.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    // 存储已加载的商品数据
    private val _loadedProducts = MutableStateFlow<Map<Long, List<Product>>>(emptyMap())

    // 当前展开的分类ID
    private val _expandedId = MutableStateFlow<Long?>(null)

    // 专门存放“新创建、未保存”分类的流
    private val _tempNewCategories = MutableStateFlow<List<CategoryWithProductsVO>>(emptyList())

    // 增加：用于在拖拽过程中保持 UI 顺序的 ID 列表流
    private val _reorderSortOrder = MutableStateFlow<List<Long>>(emptyList())

    // 用于管理拖拽排序的防抖 Job
    private var reorderJob: kotlinx.coroutines.Job? = null

    // 将数据库的 Map 转换为 UI 需要的 List
    private var _uiState: StateFlow<List<CategoryWithProductsVO>> =
        combine(
            repository.getAllCategories(),
            _tempNewCategories,
            _expandedId,
            _reorderSortOrder
        ) { dbCategories, tempList, expandedId, reorderOrder ->
            // 1. 将数据库分类转换为 VO
            val dbVOs = dbCategories.map { category ->
                CategoryWithProductsVO(
                    category = category,
                    isExpanded = category.id == expandedId,
                    isInitialEditing = false
                )
            }

            // 2. 去重合并：确保全局 ID 唯一
            val combinedList = (tempList + dbVOs).distinctBy { it.category.id }

            // 3. 排序逻辑
            if (reorderOrder.isNotEmpty()) {
                // 如果正在拖拽或刚刚拖拽完，优先按手动排序的 ID 顺序排列，防止 UI 闪跳
                combinedList.sortedBy { reorderOrder.indexOf(it.category.id) }
            } else {
                // 默认按数据库的 sort 字段排序
                combinedList.sortedBy { it.category.sort }
            }
        }.combine(_loadedProducts) { currentVOs, productsMap ->
            currentVOs.map { vo ->
                val hasData = productsMap.containsKey(vo.category.id)
                vo.copy(
                    products = productsMap[vo.category.id] ?: emptyList(),
                    isLoadingProducts = vo.isExpanded && !hasData
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<List<CategoryWithProductsVO>> = _uiState

    fun toggleCategory(categoryId: Long) {
        val nextId = if (_expandedId.value == categoryId) null else categoryId
        _expandedId.value = nextId

        if (nextId != null && !_loadedProducts.value.containsKey(categoryId)) {
            loadProductsForCategory(categoryId)
        }
    }

    private fun loadProductsForCategory(categoryId: Long) {
        viewModelScope.launch {
            val products = repository.getProductsByCategoryId(categoryId)
            _loadedProducts.update { it + (categoryId to products) }
        }
    }

    fun saveCategoryName(id: Long, newName: String) {
        viewModelScope.launch {
            repository.updateCategory(id, newName)
        }
    }

    fun saveCategory(tempId: Long, finalName: String) {
        if (finalName.isBlank()) {
            _tempNewCategories.update { list -> list.filter { it.category.id != tempId } }
            return
        }

        viewModelScope.launch {
            _tempNewCategories.update { list -> list.filter { it.category.id != tempId } }
            repository.addCategory(ProductCategory(name = finalName))
            // 保存新项后清空排序缓存，交还给数据库驱动
            _reorderSortOrder.value = emptyList()
        }
    }

    fun addNewCategory() {
        val tempId = System.currentTimeMillis()
        val newCategory = CategoryWithProductsVO(
            category = ProductCategory(id = tempId, name = ""),
            isInitialEditing = true
        )
        _tempNewCategories.value = listOf(newCategory) + _tempNewCategories.value
    }
    // 1. 内存移动：仅在拖拽过程中由 UI 频繁调用，不涉及数据库
    fun moveCategory(fromIndex: Int, toIndex: Int) {
        val currentList = _uiState.value.toMutableList()
        if (fromIndex !in currentList.indices || toIndex !in currentList.indices) return

        val movedItem = currentList.removeAt(fromIndex)
        currentList.add(toIndex, movedItem)

        // 核心：只更新 ID 顺序流，让 UI 跟着动
        _reorderSortOrder.value = currentList.map { it.category.id }
    }

    // 2. 同步到数据库：这个函数只在“松手”瞬间调用一次
    fun syncOrderToDb() {
        val currentList = _uiState.value
        viewModelScope.launch(Dispatchers.IO) {
            val updatedEntities = currentList.mapIndexed { index, vo ->
                vo.category.copy(sort = index)
            }
            repository.updateCategoriesOrder(updatedEntities)
            // 写入成功后可以考虑清空 reorderOrder，让数据库 Flow 接管顺序
            // _reorderSortOrder.value = emptyList()
        }
    }
}
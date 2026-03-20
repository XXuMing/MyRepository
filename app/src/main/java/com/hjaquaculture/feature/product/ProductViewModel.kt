package com.hjaquaculture.feature.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjaquaculture.data.local.entity.ProductEntity
import com.hjaquaculture.data.local.entity.ProductVarietyEntity
import com.hjaquaculture.data.local.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _loadedProducts = MutableStateFlow<Map<Long, List<ProductEntity>>>(emptyMap())
    private val _expandedId = MutableStateFlow<Long?>(null)

    // 专门存放“新创建、未保存”分类的流
    private val _tempNewCategories = MutableStateFlow<List<CategoryWithProductsVO>>(emptyList())

    // 用于在拖拽过程中保持 UI 顺序的 ID 列表流
    private val _reorderSortOrder = MutableStateFlow<List<Long>>(emptyList())

    val uiState: StateFlow<List<CategoryWithProductsVO>> = combine(
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
// 1. 确保 tempList 在前面
        val combinedList = (tempList + dbVOs).distinctBy { it.category.id }

        // 2. 优化排序逻辑
        if (reorderOrder.isNotEmpty()) {
            combinedList.sortedWith(compareBy<CategoryWithProductsVO> { vo ->
                // 【核心修复】：如果是临时项（isInitialEditing 为 true），排在第一优先级
                if (vo.isInitialEditing) -1 else 0
            }.thenBy { vo ->
                // 第二优先级：遵循手动排序
                val index = reorderOrder.indexOf(vo.category.id)
                if (index != -1) index else Int.MAX_VALUE
            })
        } else {
            // 默认排序：临时项在前，数据库项按 sort
            combinedList.sortedWith(compareBy<CategoryWithProductsVO> { !it.isInitialEditing }
                .thenBy { it.category.sort })
        }
    }.combine(_loadedProducts) { currentVOs, productsMap ->
        currentVOs.map { vo ->
            val hasData = productsMap.containsKey(vo.category.id)
            vo.copy(
                products = productsMap[vo.category.id] ?: emptyList(),
                isLoadingProducts = vo.isExpanded && !hasData
            )
        }
    }.flowOn(Dispatchers.Default) // 由于页面有些卡顿，改为：排序和过滤不占主线程
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


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

    // 保存新分类
    fun saveCategory(tempId: Long, finalName: String) {
        if (finalName.isBlank()) {
            _tempNewCategories.update { list -> list.filter { it.category.id != tempId } }
            return
        }

        viewModelScope.launch {
            // 获取当前数据库中最小的 sort 值
            val currentCategories = uiState.value
            val minSort = currentCategories.minOfOrNull { it.category.sort } ?: 0

            _tempNewCategories.update { list -> list.filter { it.category.id != tempId } }

            // 为了让它在最前面，新项的 sort 应该比当前最小的还要小
            // 或者更简单的做法：插入后统一调用一次 syncOrderToDb 重新刷一遍 index
            repository.addCategory(ProductVarietyEntity(name = finalName, sort = minSort - 1))

            _reorderSortOrder.value = emptyList()

            // 强烈建议：保存后重新对数据库所有项进行一次 sort 归一化（0, 1, 2...）
            // 这样可以防止 sort 变成极小的负数
            syncOrderToDb()
        }
    }

    // 新增分类：临时置于列表首位
    fun addNewCategory() {
        // 使用负数 ID 或大时间戳标记临时项
        val tempId = System.currentTimeMillis()
        val newCategory = CategoryWithProductsVO(
            category = ProductVarietyEntity(id = tempId, name = "", sort = -1), // 给予最小 sort 预设
            isInitialEditing = true
        )
        // 核心修改：确保它在临时列表的首位
        _tempNewCategories.update { listOf(newCategory) + it }
    }

    fun moveCategory(fromIndex: Int, toIndex: Int) {
        val currentList = uiState.value.toMutableList()
        if (fromIndex !in currentList.indices || toIndex !in currentList.indices) return

        val movedItem = currentList.removeAt(fromIndex)
        currentList.add(toIndex, movedItem)

        _reorderSortOrder.value = currentList.map { it.category.id }
    }

    fun syncOrderToDb() {
        val currentList = uiState.value
        viewModelScope.launch(Dispatchers.IO) {
            // 重新计算所有项的 sort 顺序，0 为最顶端
            val updatedEntities = currentList.mapIndexed { index, vo ->
                vo.category.copy(sort = index)
            }
            repository.updateCategoriesOrder(updatedEntities)
        }
    }
}
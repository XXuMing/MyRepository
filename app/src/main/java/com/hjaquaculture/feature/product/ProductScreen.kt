package com.hjaquaculture.feature.product

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hjaquaculture.R
import com.hjaquaculture.data.local.entity.ProductEntity
import com.hjaquaculture.feature.AuthAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductScreen(
    vm: ProductViewModel = hiltViewModel(),
    onAction: (AuthAction) -> Unit,
    scaffoldPadding: PaddingValues,
) {
    val categories by vm.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        vm.moveCategory(from.index, to.index)
    }

    // 列表是否可见，用于延迟加载，改善掉帧
    var isListVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isListVisible = true // 甚至不需要 delay，这会让出首帧
    }

    LaunchedEffect(reorderableState.isAnyItemDragging) {
        if (!reorderableState.isAnyItemDragging) {
            vm.syncOrderToDb()
        }
    }

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
            .fillMaxSize()
            .padding(scaffoldPadding)
            .padding(horizontal = 8.dp),
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = { /* 占位功能 */ }) {
                    Icon(Icons.Filled.AddCard, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = {
                    // 1. 彻底清除现有焦点，防止键盘在不该出现的时候干扰计算
                    focusManager.clearFocus()

                    scope.launch {
                        // 2. 第一步：先将现有列表滚到顶（此时新项还没出，干扰最少）
                        lazyListState.animateScrollToItem(0)

                        // 3. 第二步：添加新分类（此时会触发 UI 刷新，新项出现在 index 0）
                        vm.addNewCategory()

                        // 4. 第三步：关键延迟
                        // 给 Compose 几毫秒时间去测量新加入的那个 Item 的高度
                        // 也给键盘弹起留出一定的缓冲时间
                        delay(100)

                        // 5. 第四步：最终纠偏
                        // 此时 index 0 已经是那个带输入框的新卡片了，再次强制置顶
                        lazyListState.animateScrollToItem(0)
                    }
                }) {
                    Icon(painterResource(R.drawable.contextual_token_add_24px), contentDescription = "添加分类")
                }
            }
        }
    ) { paddingValues ->
        Column(){
            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = { Text("搜索") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
            )
            Spacer(modifier = Modifier.height(8.dp))

            if(isListVisible){
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize().imePadding(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        categories,
                        key = { it.category.id },
                        contentType = { it.isInitialEditing }
                    ) { item ->
                        ReorderableItem(reorderableState, key = item.category.id) { isDragging ->
                            val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp)

                            CategoryGroupCard(
                                state = item,
                                modifier = Modifier
                                    .shadow(elevation)
                                    .longPressDraggableHandle()
                                    .clickable {
                                        focusManager.clearFocus()
                                        vm.toggleCategory(item.category.id)
                                    },
                                onProductClick = { /* TODO: 跳转商品详情 */ },
                                onSaveName = { newName ->
                                    val isInitial = item.isInitialEditing
                                    if (isInitial) {
                                        vm.saveCategory(item.category.id, newName)
                                        // 重点修复：保存新分类后，自动滚动到顶端
                                        scope.launch {
                                            delay(150) // 等待数据库写入和 UI 重排完成
                                            lazyListState.animateScrollToItem(0)
                                        }
                                    } else {
                                        vm.saveCategoryName(item.category.id, newName)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGroupCard(
    state: CategoryWithProductsVO,
    modifier: Modifier = Modifier,
    onProductClick: (Long) -> Unit,
    onSaveName: (String) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Column {
            CategoryHeader(
                state = state,
                onConfirm = onSaveName
            )

            AnimatedVisibility(
                visible = state.isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (state.isLoadingProducts) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp).size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else if (state.products.isEmpty()) {
                        Text(
                            "该分类下暂无商品",
                            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    } else {
                        FlowRow(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()) {
                            state.products.forEach { product ->
                                ProductItemButton(product = product, onClick = onProductClick)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    state: CategoryWithProductsVO,
    onConfirm: (String) -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (state.isExpanded) 180f else 0f)

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            EditableText(
                initialText = state.category.name,
                autoFocus = state.isInitialEditing,
                onConfirm = onConfirm
            )
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier.rotate(rotation),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun EditableText(
    initialText: String,
    autoFocus: Boolean = false,
    onConfirm: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(autoFocus) }
    var textValue by remember { mutableStateOf(TextFieldValue(initialText)) }
    val focusManager = LocalFocusManager.current

    fun handleConfirm() {
        if (isEditing) {
            isEditing = false
            onConfirm(textValue.text)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(40.dp)
            .pointerInput(Unit) {
                detectTapGestures { /* 仅仅消耗事件 */ }
            }
    ) {
        AnimatedContent(targetState = isEditing, label = "EditTransition") { editing ->
            if (editing) {
                val focusRequester = remember { FocusRequester() }
                var hasGainedFocus by remember { mutableStateOf(false) }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { /* TODO: 删除 */ }) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                    BasicTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        singleLine = true,
                        modifier = Modifier
                            .widthIn(min = 50.dp, max = 150.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    hasGainedFocus = true
                                } else if (hasGainedFocus) {
                                    handleConfirm()
                                }
                            },
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                handleConfirm()
                                focusManager.clearFocus()
                            }
                        )
                    )
                    IconButton(onClick = {
                        handleConfirm()
                        focusManager.clearFocus()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                    }

                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                        textValue = textValue.copy(selection = TextRange(textValue.text.length))
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                    Text(
                        textValue.text.ifEmpty { "未命名分类" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductItemButton(product: ProductEntity, onClick: (Long) -> Unit) {
    val focusManager = LocalFocusManager.current
    OutlinedButton(
        onClick = {
            focusManager.clearFocus()
            onClick(product.id)
        },
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = product.name)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "¥${product.currentPrice / 100.0}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
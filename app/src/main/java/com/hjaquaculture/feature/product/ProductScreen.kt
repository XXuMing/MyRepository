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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hjaquaculture.data.local.entity.Product
import com.hjaquaculture.data.local.entity.ProductCategory
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val categories by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()

    // 新版 Reorderable 初始化
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        viewModel.moveCategory(from.index, to.index)
    }

    Scaffold(
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = { viewModel.addNewCategory() }) {
                    Icon(Icons.Filled.AddCard, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = { /* 其他功能 */ }) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text("搜索") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = MaterialTheme.shapes.medium
            )

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize().imePadding(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(categories, key = { it.category.id }) { item ->
                    ReorderableItem(reorderableState, key = item.category.id) { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp)

                        CategoryGroupCard(
                            state = item,
                            modifier = Modifier.shadow(elevation),
                            onToggle = { viewModel.toggleCategory(item.category.id) },
                            onProductClick = { /* TODO */ },
                            // 注意：新版 handle 逻辑通过 modifier 注入
                            dragHandleModifier = Modifier.draggableHandle(),
                            onSaveName = { newName ->
                                if (item.isInitialEditing) {
                                    viewModel.saveCategory(item.category.id, newName)
                                } else {
                                    viewModel.saveCategoryName(item.category.id, newName)
                                }
                            }
                        )
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
    dragHandleModifier: Modifier = Modifier, // 接收拖拽手柄修饰符
    onToggle: () -> Unit,
    onProductClick: (Long) -> Unit,
    onSaveName: (String) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp).clickable { onToggle() }
    ) {
        Column {
            CategoryHeader(
                state = state,
                onConfirm = onSaveName,
                dragHandleModifier = dragHandleModifier
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
    dragHandleModifier: Modifier,
    onConfirm: (String) -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (state.isExpanded) 180f else 0f)

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 关键点：将 dragHandleModifier 绑定给这个 Icon
        Icon(
            imageVector = Icons.Default.DragHandle,
            contentDescription = "Drag",
            modifier = dragHandleModifier
                .padding(end = 12.dp)// 关键：阻止手柄处的点击事件向上传递给 Card
                .pointerInput(Unit) {
                    detectTapGestures { /* 消耗掉点击，不执行任何操作 */ }
                },
            tint = Color.Gray
        )

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
    val focusRequester = remember { FocusRequester() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(40.dp)
    ) {
        AnimatedContent(targetState = isEditing, label = "") { editing ->
            if (editing) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                    BasicTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        singleLine = true,
                        modifier = Modifier
                            .widthIn(min = 50.dp, max = 150.dp)
                            .focusRequester(focusRequester),
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface)
                    )
                    IconButton(onClick = {
                        isEditing = false
                        onConfirm(textValue.text)
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
private fun ProductItemButton(product: Product, onClick: (Long) -> Unit) {
    OutlinedButton(
        onClick = { onClick(product.id) },
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
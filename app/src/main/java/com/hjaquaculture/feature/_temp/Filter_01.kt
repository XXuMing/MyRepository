package com.hjaquaculture.feature._temp

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// --- 1. 数据模型 ---
data class OrderItem(
    val id: String,
    val status: String,
    val type: String,
    val price: String
)

data class FilterOption(
    val status: String = "全部",
    val type: String = "全部",
    val searchQuery: String = ""
)

// 假数据源
val mockOrders = listOf(
    OrderItem("ORD-2024001", "未付", "自取", "￥50.00"),
    OrderItem("ORD-2024002", "已付", "寄件", "￥128.50"),
    OrderItem("ORD-2024003", "已付", "自取", "￥35.00"),
    OrderItem("ORD-2024004", "已付", "寄件", "￥210.00"),
    OrderItem("ORD-2024005", "未付", "寄件", "￥99.00"),
)

// --- 2. 可复用的封装组件 ---

@Preview
@Composable
fun PrevFilterGroup() {
    val item1 = listOf("全部", "未付", "已付")

    FilterGroup("ceui",item1,"全部",{})
}
/**
 * 核心过滤组：封装了标题和 Chip 流式布局
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterGroup(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = (selectedOption == option),
                    onClick = { onOptionSelected(option) },
                    label = { Text(option) },
                    leadingIcon = if (selectedOption == option) {
                        { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)) }
                    } else null
                )
            }
        }
    }
}

/**
 * 模式一：Inline 展开式过滤栏
 */
@Composable
fun InlineFilterPanel(
    isVisible: Boolean,
    currentOption: FilterOption,
    onChanged: (FilterOption) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                FilterGroup("订单状态", listOf("全部", "未付", "已付"), currentOption.status) {
                    onChanged(currentOption.copy(status = it))
                }
                FilterGroup("配送方式", listOf("全部", "自取", "寄件"), currentOption.type) {
                    onChanged(currentOption.copy(type = it))
                }
            }
        }
    }
}

/**
 * 模式二：ModalBottomSheet 封装
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetWrapper(
    showSheet: Boolean,
    currentOption: FilterOption,
    onDismiss: () -> Unit,
    onChanged: (FilterOption) -> Unit
) {
    if (showSheet) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).padding(bottom = 40.dp)) {
                Text("高级筛选", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                FilterGroup("订单状态", listOf("全部", "未付", "已付"), currentOption.status) {
                    onChanged(currentOption.copy(status = it))
                }
                FilterGroup("配送方式", listOf("全部", "自取", "寄件"), currentOption.type) {
                    onChanged(currentOption.copy(type = it))
                }
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    Text("完成")
                }
            }
        }
    }
}

// --- 3. 主页面 ---

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFilterScreen() {
    var filter by remember { mutableStateOf(FilterOption()) }
    var isInlineOpen1 by remember { mutableStateOf(false) }
    var isInlineOpen2 by remember { mutableStateOf(false) }
    var isInlineOpen3 by remember { mutableStateOf(false) }
    var isInlineOpen4 by remember { mutableStateOf(false) }
    var isSheetOpen by remember { mutableStateOf(false) }

    // 搜索过滤后的列表数据
    val displayList = remember(filter) {
        mockOrders.filter {
            (filter.status == "全部" || it.status == filter.status) &&
                    (filter.type == "全部" || it.type == filter.type) &&
                    (it.id.contains(filter.searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的订单") },
                actions = {
                    // 切换底部弹窗模式
                    IconButton(onClick = { isSheetOpen = true }) {
                        Icon(Icons.Default.Tune, contentDescription = "高级筛选")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // 搜索框 + 展开切换按钮
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = filter.searchQuery,
                    onValueChange = { filter = filter.copy(searchQuery = it) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("搜索订单号") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    onClick = { isInlineOpen1 = !isInlineOpen1 },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isInlineOpen1) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = if (isInlineOpen1) Icons.Default.FilterListOff else Icons.Default.FilterList,
                        contentDescription = "切换过滤面板"
                    )
                }
                FilledIconButton(
                    onClick = { isInlineOpen2 = !isInlineOpen2 },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isInlineOpen2) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = if (isInlineOpen2) Icons.Default.FilterListOff else Icons.Default.FilterList,
                        contentDescription = "切换过滤面板"
                    )
                }
                FilledIconButton(
                    onClick = { isInlineOpen3 = !isInlineOpen3 },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isInlineOpen3) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = if (isInlineOpen3) Icons.Default.FilterListOff else Icons.Default.FilterList,
                        contentDescription = "切换过滤面板"
                    )
                }
                FilledIconButton(
                    onClick = { isInlineOpen4 = !isInlineOpen4 },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isInlineOpen4) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = if (isInlineOpen4) Icons.Default.FilterListOff else Icons.Default.FilterList,
                        contentDescription = "切换过滤面板"
                    )
                }
            }

            // 1. Inline 展开式组件调用
            InlineFilterPanel(
                isVisible = isInlineOpen1,
                currentOption = filter,
                onChanged = { filter = it }
            )
            InlineFilterPanelSpring(
                isVisible = isInlineOpen2,
                currentOption = filter,
                onChanged = { filter = it }
            )
            InlineFilterPanelStaggered(
                isVisible = isInlineOpen3,
                currentOption = filter,
                onChanged = { filter = it }
            )
            InlineFilterPanelScale(
                isVisible = isInlineOpen4,
                currentOption = filter,
                onChanged = { filter = it }
            )

            // 2. 列表区域
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(displayList) { order ->
                    ListItem(
                        headlineContent = { Text(order.id) },
                        supportingContent = { Text("${order.status} · ${order.type}") },
                        trailingContent = {
                            Text(order.price, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                        },
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                }

                if (displayList.isEmpty()) {
                    item {
                        Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            Text("暂无符合条件的订单", color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }
        }

        // 3. BottomSheet 组件调用
        FilterBottomSheetWrapper(
            showSheet = isSheetOpen,
            currentOption = filter,
            onDismiss = { isSheetOpen = false },
            onChanged = { filter = it }
        )
    }
}

// 更多动画效果

// 弹簧动力面板 (Spring Expansion)
@Composable
fun InlineFilterPanelSpring(
    isVisible: Boolean,
    currentOption: FilterOption,
    onChanged: (FilterOption) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy, // 低阻尼，产生回弹
                stiffness = Spring.StiffnessLow          // 低刚度，动作更慢更柔和
            )
        ) + fadeIn(),
        exit = shrinkVertically(
            animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy)
        ) + fadeOut()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                FilterGroup("订单状态", listOf("全部", "未付", "已付"), currentOption.status) {
                    onChanged(currentOption.copy(status = it))
                }
                FilterGroup("配送方式", listOf("全部", "自取", "寄件"), currentOption.type) {
                    onChanged(currentOption.copy(type = it))
                }
            }
        }
    }
}

// 级联淡入面板 (Staggered Animation)
@Composable
fun InlineFilterPanelStaggered(
    isVisible: Boolean,
    currentOption: FilterOption,
    onChanged: (FilterOption) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(16.dp)
        ) {
            // 第一个组件使用较快的弹簧
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -20 })
            ) {
                FilterGroup("订单状态", listOf("全部", "未付", "已付"), currentOption.status) {
                    onChanged(currentOption.copy(status = it))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 第二个组件稍微延迟一点
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(delayMillis = 100)) +
                        slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessLow))
            ) {
                FilterGroup("配送方式", listOf("全部", "自取", "寄件"), currentOption.type) {
                    onChanged(currentOption.copy(type = it))
                }
            }
        }
    }
}

// 仿 iOS 抽屉式缩放 (Scale & Bounce)
@Composable
fun InlineFilterPanelScale(
    isVisible: Boolean,
    currentOption: FilterOption,
    onChanged: (FilterOption) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(initialScale = 0.9f, animationSpec = spring(Spring.DampingRatioMediumBouncy)) + fadeIn(),
        exit = scaleOut(targetScale = 0.9f) + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(16.dp)
        ) {
            // 第一个组件使用较快的弹簧
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -20 })
            ) {
                FilterGroup("订单状态", listOf("全部", "未付", "已付"), currentOption.status) {
                    onChanged(currentOption.copy(status = it))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 第二个组件稍微延迟一点
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(delayMillis = 100)) +
                        slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessLow))
            ) {
                FilterGroup("配送方式", listOf("全部", "自取", "寄件"), currentOption.type) {
                    onChanged(currentOption.copy(type = it))
                }
            }
        }
    }
}
package com.hjaquaculture.feature.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


// =============================================================================
// 一、数据契约（Data Contract）
// =============================================================================

/**
 * 描述"一组过滤选项"的配置数据类。
 *
 * 使用泛型 <T> 让它能适配任何枚举，例如：
 *   - FilterGroupConfig<OrderStatus>(...)
 *   - FilterGroupConfig<InvoiceStatus>(...)
 *   - FilterGroupConfig<PaymentMethods>(...)
 *
 * @param T       枚举类型，必须实现 [FilterableOption] 接口（见下方）
 * @param title   该组过滤的标题，例如："订单状态"、"付款方式"
 * @param options 该组所有可选项（通常传入枚举的 entries 列表）
 * @param selected 当前选中的值（null 表示"全部/不过滤"）
 * @param onSelect 用户点击某个选项时的回调，null 表示取消选中（选"全部"）
 */
data class FilterGroupConfig<T : FilterableOption>(
    val title: String,
    val options: List<T>,
    val selected: T?,
    val onSelect: (T?) -> Unit
)

/**
 * 过滤选项接口（Interface）。
 *
 * 你的枚举只需实现这一个接口，提供一个"显示给用户看的标签文字"即可。
 * 例如：
 *   enum class OrderStatus(...) : FilterableOption {
 *       override val label get() = description
 *   }
 *
 * 这样 SearchFilterBar 就不需要关心枚举的具体类型。
 */
interface FilterableOption {
    val label: String
}


// =============================================================================
// 二、主组件 SearchFilterBar
// =============================================================================

/**
 * 通用搜索 + 过滤栏组件。
 *
 * ┌─────────────────────────────────────┬───────┐
 * │  🔍  搜索文本框（OutlinedTextField）   │  🔽  │  ← FilledIconButton
 * └─────────────────────────────────────┴───────┘
 *   ↓ 点击按钮后，以动画展开下方面板
 * ┌─────────────────────────────────────────────┐
 * │  订单状态：[草稿] [预定] [确认✓] [完成] [作废] │
 * │  付款方式：[微信] [支付宝✓] [现金] [其他]     │
 * └─────────────────────────────────────────────┘
 *
 * 使用示例（在 OrderScreen 中）：
 * ```kotlin
 * SearchFilterBar(
 *     searchQuery = uiState.searchQuery,
 *     onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
 *     searchPlaceholder = "搜索订单号",
 *     filterGroups = listOf(
 *         FilterGroupConfig(
 *             title = "订单状态",
 *             options = OrderStatus.entries,   // 传入所有枚举值
 *             selected = uiState.selectedStatus,
 *             onSelect = { viewModel.onStatusSelected(it) }
 *         ),
 *         FilterGroupConfig(
 *             title = "订单类型",
 *             options = OrderSymbol.entries,
 *             selected = uiState.selectedSymbol,
 *             onSelect = { viewModel.onSymbolSelected(it) }
 *         )
 *     )
 * )
 * ```
 *
 * @param searchQuery         当前搜索框的文字内容（由外部状态控制）
 * @param onSearchQueryChange 搜索框文字变化时的回调
 * @param searchPlaceholder   搜索框的占位提示文字，不同页面可定制
 * @param filterGroups        过滤组配置列表，每个元素对应面板里的一行 Chip
 * @param modifier            外部可传入 Modifier 调整整体布局
 */
@Composable
fun SearchFilterBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchPlaceholder: String = "搜索...",
    filterGroups: List<FilterGroupConfig<*>>,  // * 表示任意实现了 FilterableOption 的泛型
    modifier: Modifier = Modifier
) {
    // ── 状态：面板是否展开 ──────────────────────────────────────────────────
    // remember { } 让这个状态在重组（recomposition）时保持不变
    var isPanelExpanded by remember { mutableStateOf(false) }

    // ── 动画：按钮图标旋转角度（展开时旋转 180°，有视觉反馈） ────────────────
    // animateFloatAsState 会在 isPanelExpanded 变化时，自动用动画过渡 Float 值
    val iconRotation by animateFloatAsState(
        targetValue = if (isPanelExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "filterIconRotation"
    )

    // ── 判断是否有任何过滤条件被选中（用于高亮按钮颜色） ──────────────────────
    val hasActiveFilter = filterGroups.any { it.selected != null }

    // ── 整体容器，用 animateContentSize 让高度变化有动画 ─────────────────────
    // animateContentSize() 会自动侦测子内容尺寸变化并做动画过渡
    // 注意：它必须加在会发生尺寸变化的 Composable 上
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(durationMillis = 300)
            )
    ) {

        // ── 第一行：搜索框 + 过滤按钮 ─────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 左侧：搜索文本框
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                // weight(1f) 让搜索框占满剩余宽度，把按钮挤到右边
                modifier = Modifier.weight(1f),
                placeholder = { Text(searchPlaceholder) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索图标"
                    )
                },
                // singleLine = true 防止多行输入撑高布局
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 右侧：过滤按钮（FilledIconButton）
            FilledIconButton(
                onClick = {
                    // 点击时切换展开/收起状态
                    isPanelExpanded = !isPanelExpanded
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    // 逻辑：有激活的过滤条件 → 用醒目的 primary 颜色
                    //       面板展开中 → 用 primaryContainer 颜色
                    //       默认 → 用 surfaceVariant 低调颜色
                    containerColor = when {
                        hasActiveFilter -> MaterialTheme.colorScheme.primary
                        isPanelExpanded -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = when {
                        hasActiveFilter -> MaterialTheme.colorScheme.onPrimary
                        isPanelExpanded -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = if (isPanelExpanded) "收起过滤面板" else "展开过滤面板",
                    // rotate() 配合 animateFloatAsState 实现旋转动画
                    modifier = Modifier.rotate(iconRotation)
                )
            }
        }

        // ── 第二部分：过滤面板（AnimatedVisibility 控制显示/隐藏） ────────────
        // AnimatedVisibility 会在 visible 变化时，执行 enter/exit 动画
        AnimatedVisibility(
            visible = isPanelExpanded,
            // enter：从顶部展开 + 淡入
            enter = expandVertically(expandFrom = Alignment.Top, animationSpec = tween(durationMillis = 150)) + fadeIn(animationSpec = tween(durationMillis = 150)),
            // exit：向顶部收起 + 淡出
            exit = shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(durationMillis = 120)) + fadeOut(animationSpec = tween(durationMillis = 120))
        ) {
            // 面板背景 Surface，给一点视觉层次感
            Surface(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                // 风格与ElevatedCard相似，ElevatedCard默认使用 shapes.medium
                shape = MaterialTheme.shapes.medium,
                // tonalElevation 在 Material3 中会根据主题自动叠加一层色调
                tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp,bottom = 8.dp,start = 12.dp,end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp) // 每组之间的间距
                ) {
                    // 遍历所有过滤组配置，每个渲染一行
                    // 注意：这里用 @Suppress 是因为 Kotlin 泛型擦除，
                    // filterGroups 是 List<FilterGroupConfig<*>>，
                    // 我们需要用辅助函数来安全地渲染它
                    filterGroups.forEach { config ->
                        FilterGroupRow(config = config)
                    }
                }
            }
        }
    }
}


// =============================================================================
// 三、子组件 FilterGroupRow（一行过滤标签 + Chips）
// =============================================================================

/**
 * 渲染单行过滤组：标题 + 横向滚动的 FilterChip 列表。
 *
 * 注意这里使用 @Suppress("UNCHECKED_CAST")：
 * 因为 filterGroups 存的是 FilterGroupConfig<*>（星号投影），
 * 编译器无法确定泛型类型，但我们通过接口约束保证了类型安全。
 *
 * @param config 一组过滤配置（标题、选项列表、当前选中值、选中回调）
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T : FilterableOption> FilterGroupRow(config: FilterGroupConfig<T>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = config.title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // ✅ 改为 FlowRow，自动换行，所有选项一次可见
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // 换行后行间距
        ) {
            // "全部" Chip
            FilterChip(
                selected = config.selected == null,
                onClick = { config.onSelect.invoke(null) },
                label = { Text("全部") },
                leadingIcon = if (config.selected == null) {
                    { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) }
                } else null
            )

            // 枚举选项 Chips
            config.options.forEach { option ->
                val isSelected = config.selected == option
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val newValue = if (isSelected) null else option
                        config.onSelect.invoke(newValue)
                    },
                    label = { Text(option.label) },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) }
                    } else null
                )
            }
        }
    }
}

// 添加到 SearchFilterBar.kt 文件末尾

// =============================================================================
// 四、预览函数
// =============================================================================

// 临时枚举，仅用于 Preview，不需要添加到项目的枚举文件中
private enum class PreviewOrderStatus(override val label: String) : FilterableOption {
    CANCELLED("作废"),
    DRAFT("草稿"),
    RESERVATION("预定"),
    CONFIRMED("确认"),
    COMPLETED("完成")
}

private enum class PreviewPaymentMethods(override val label: String) : FilterableOption {
    WECHAT("微信"),
    ALIPAY("支付宝"),
    CASH("现金"),
    OTHER("其他")
}

@Preview(showBackground = true, name = "默认状态（面板收起）")
@Composable
private fun SearchFilterBarPreview_Collapsed() {
    MaterialTheme {
        SearchFilterBar(
            searchQuery = "",
            onSearchQueryChange = {},
            searchPlaceholder = "搜索订单号",
            filterGroups = listOf(
                FilterGroupConfig(
                    title = "订单状态",
                    options = PreviewOrderStatus.entries,
                    selected = null,  // 无选中
                    onSelect = {}
                ),
                FilterGroupConfig(
                    title = "付款方式",
                    options = PreviewPaymentMethods.entries,
                    selected = null,
                    onSelect = {}
                )
            )
        )
    }
}

@Preview(showBackground = true, name = "面板展开 + 有过滤条件")
@Composable
private fun SearchFilterBarPreview_Expanded() {
    // 用 remember 模拟状态，让 Chip 的选中样式在预览中正常渲染
    var selectedStatus by remember { mutableStateOf<PreviewOrderStatus?>(PreviewOrderStatus.CONFIRMED) }
    var selectedPayment by remember { mutableStateOf<PreviewPaymentMethods?>(null) }

    MaterialTheme {
        SearchFilterBar(
            searchQuery = "SO250101",
            onSearchQueryChange = {},
            searchPlaceholder = "搜索订单号",
            filterGroups = listOf(
                FilterGroupConfig(
                    title = "订单状态",
                    options = PreviewOrderStatus.entries,
                    selected = selectedStatus,
                    onSelect = { selectedStatus = it }
                ),
                FilterGroupConfig(
                    title = "付款方式",
                    options = PreviewPaymentMethods.entries,
                    selected = selectedPayment,
                    onSelect = { selectedPayment = it }
                ),
            )
        )
    }
}
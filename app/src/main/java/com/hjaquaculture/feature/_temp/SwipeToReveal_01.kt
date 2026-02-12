package com.hjaquaculture.feature._temp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// 1. 定义三段式状态枚举
enum class DragValue {
    Closed,     // 关闭
    HalfOpen,   // 露出第一个按钮（删除）
    FullOpen    // 露出全部按钮（删除+编辑）
}

/**
 * 侧滑组件：支持编辑和删除，包含自动收起逻辑
 * * @param isOpened 外部控制：当前项是否应该是打开状态
 * @param onOpenRequest 外部回调：当用户手动划开此项时通知父组件
 * @param onCloseRequest 外部回调：当此项需要关闭时通知父组件
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToRevealItem(
    isOpened: Boolean,
    onOpenRequest: () -> Unit,
    onCloseRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // 按钮配置
    val buttonWidth = 80.dp
    val buttonWidthPx = with(density) { buttonWidth.toPx() }

    // 2. 初始化 AnchoredDraggableState
    // 注意：新版构造函数极其精简，不传 animationSpec 等参数以避免版本不匹配报错
    val state = remember {
        AnchoredDraggableState(initialValue = DragValue.Closed)
    }

    // 3. 核心：侧滑互斥逻辑监听
    // 3. 互斥监听：如果外部要求关闭，强制回到 Closed
    LaunchedEffect(isOpened) {
        if (!isOpened) {
            state.animateTo(DragValue.Closed)
        }
    }

    // 4. 更新锚点：定义三个精确的物理停靠点
    LaunchedEffect(buttonWidthPx) {
        state.updateAnchors(
            DraggableAnchors {
                DragValue.Closed at 0f
                DragValue.HalfOpen at -buttonWidthPx       // 第一段位置
                DragValue.FullOpen at -(buttonWidthPx * 2) // 第二段位置
            }
        )
    }

    // 5. 【优化状态反馈】
    // 当 state.targetValue 发生变化时就通知外部，而不是等动画结束
    // 这样可以极大地提升互斥响应的速度，防止“卡住”
    LaunchedEffect(state.targetValue) {
        if (state.targetValue != DragValue.Closed) {
            onOpenRequest()
        }
    }

    // 6. 配置 FlingBehavior (滑动行为)
    // 修复点：移除报错的 velocityThreshold, snapAnimationSpec, decayAnimationSpec
    // 使用默认参数以适配 BOM 2025.12
    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = state,
        // 这里可以根据需要微调灵敏度
        positionalThreshold = { distance -> distance * 0.5f }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // --- 底层：按钮组 ---
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        ) {
            // 删除按钮
            Box(
                modifier = Modifier
                    .width(buttonWidth)
                    .fillMaxHeight()
                    .background(Color.Red)
                    .clickable {
                        onDeleteClick()
                        // 逻辑：点击后自动收起
                        scope.launch { state.animateTo(DragValue.Closed) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                    Text("删除", color = Color.White, fontSize = 12.sp)
                }
            }

            // 编辑按钮
            Box(
                modifier = Modifier
                    .width(buttonWidth)
                    .fillMaxHeight()
                    .background(Color(0xFF4CAF50))
                    .clickable {
                        onEditClick()
                        // 逻辑：点击后自动收起
                        scope.launch { state.animateTo(DragValue.Closed) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                    Text("编辑", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        // --- 上层：内容主体 ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    // 修复点：初始化检查，防止 requireOffset 崩溃
                    val x = if (state.anchors.size > 0) state.requireOffset() else 0f
                    IntOffset(x.roundToInt(), 0)
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    flingBehavior = flingBehavior
                )
                .background(Color.White)
                // 逻辑：点击内容区域，如果已打开则收起
                .clickable {
                    if (state.currentValue != DragValue.Closed) {
                        scope.launch { state.animateTo(DragValue.Closed) }
                    }
                }
        ) {
            content()
        }
    }
}

/**
 * 列表 Demo：展示侧滑互斥逻辑
 */
@Composable
fun SwipeableListScreen() {
    val items = remember { mutableStateListOf(*(1..20).map { "消息项 $it" }.toTypedArray()) }

    // 7. 互斥逻辑的核心：记录当前哪个 ID 是打开的
    var openedItemId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it }) { item ->
            SwipeToRevealItem(
                // 如果当前 ID 等于记录的 ID，则该项处于 Open 状态
                // 互斥逻辑的核心：判断当前项是否匹配开启 ID
                isOpened = openedItemId == item,
                onOpenRequest = {
                    // 当滑动此项时，更新全局开启 ID，其他项会自动收起
                    openedItemId = item
                },
                onCloseRequest = {
                    if (openedItemId == item) openedItemId = null
                },
                onEditClick = { /* 执行编辑 */ },
                onDeleteClick = { items.remove(item) }
            ) {
                // 列表项内容 UI
                ListItem(
                    headlineContent = { Text(item, style = MaterialTheme.typography.titleMedium) },
                    supportingContent = { Text("二段侧滑：删->编", fontSize = 12.sp) },
                    leadingContent = {
                        Box(Modifier.size(40.dp).background(Color.LightGray, RoundedCornerShape(20.dp)))
                    }
                )
            }
        }
    }
}

/**
 * 列表使用示例
 */
@Composable
fun SwipeToReveal_01() {
    SwipeableListScreen()
}

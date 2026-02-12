package com.hjaquaculture.feature._temp
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
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

/**
 * 优化版二段式侧滑组件
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToRevealItem2(
    isOpened: Boolean, // 外部传入的开关状态，用于实现互斥
    onOpenRequest: () -> Unit, // 用户开始滑动或手动打开的回调
    onCloseRequest: () -> Unit, // 用户手动关闭或需要重置的回调
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // 按钮宽度：每个 80dp
    val buttonWidth = 80.dp
    val buttonWidthPx = with(density) { buttonWidth.toPx() }

    // 2. 初始化 State (适配 1.7+，不传已弃用的参数如 velocityThreshold)
    // 使用 remember(key) 可以在 ID 变化时强制重置，防止卡顿
    val state = remember {
        AnchoredDraggableState(initialValue = DragValue.Closed)
    }

    // 3. 【加固互斥逻辑】
    // 当外部 isOpened 变为 false 时，立即执行动画收回
    // LaunchedEffect 会在 isOpened 变化时自动取消上一次未完成的动画任务
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

    // 6. 配置滑动行为 (去掉了所有会报错的参数名)
    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = state,
        positionalThreshold = { distance -> distance * 0.5f }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(8.dp))
    ) {
        // --- 底层：按钮组 (Row 排列) ---
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        ) {
            // 编辑按钮 (后露出)
            Box(
                modifier = Modifier
                    .width(buttonWidth)
                    .fillMaxHeight()
                    .background(Color(0xFF4CAF50))
                    .clickable {
                        onEditClick()
                        scope.launch { state.animateTo(DragValue.Closed) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Edit, null, tint = Color.White)
                    Text("编辑", color = Color.White, fontSize = 12.sp)
                }
            }

            // 删除按钮 (先露出)
            Box(
                modifier = Modifier
                    .width(buttonWidth)
                    .fillMaxHeight()
                    .background(Color.Red)
                    .clickable {
                        onDeleteClick()
                        scope.launch { state.animateTo(DragValue.Closed) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Delete, null, tint = Color.White)
                    Text("删除", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        // --- 上层：内容主体 ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    // 7. 安全检查：如果锚点没准备好，不读取 offset，防止崩溃
                    val x = if (state.anchors.size > 0) state.requireOffset() else 0f
                    IntOffset(x.roundToInt(), 0)
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    flingBehavior = flingBehavior
                )
                .background(Color.White)
                // 逻辑：点击内容区域时，如果已打开，则收起
                .clickable(enabled = state.currentValue != DragValue.Closed) {
                    scope.launch { state.animateTo(DragValue.Closed) }
                }
        ) {
            content()
        }
    }
}

/**
 * 列表使用示例
 */
@Composable
fun FinalSwipeListScreen() {
    val items = remember { mutableStateListOf(*(1..20).map { "联系人 $it" }.toTypedArray()) }

    // 8. 列表级唯一状态记录：当前开启的项 ID
    var openedItemId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it }) { item ->
            SwipeToRevealItem2(
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

@Composable
fun SwipeToReveal_02(){
    FinalSwipeListScreen()
}
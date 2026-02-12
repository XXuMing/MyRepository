package com.hjaquaculture.feature.relationship

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun SupplierScreen() {
    SimpleSwipeableCard({})
}

@Composable
fun SimpleSwipeableCard(
    onDelete: () -> Unit
) {
    val density = LocalDensity.current
    val actionBoxWidth = 140.dp
    val actionBoxWidthPx = with(density) { actionBoxWidth.toPx() }

    // 1. 使用简单的 Float 记录偏移量
    var offsetX by remember { mutableFloatStateOf(0f) }

    // 2. 使用动画值来平滑回弹
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "offset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F1F1))
    ) {
        // 底层：删除按钮
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(actionBoxWidth)
                .background(Color.Red)
                .align(Alignment.CenterEnd)
                .clickable {
                    offsetX = 0f // 重置状态
                    onDelete()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
        }

        // 上层：内容卡片
        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            // 3. 释放后的逻辑：过半则开，否则关
                            offsetX = if (offsetX < -actionBoxWidthPx / 2) {
                                -actionBoxWidthPx
                            } else {
                                0f
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            // 4. 计算偏移并限制滑动范围
                            val newOffset = offsetX + dragAmount
                            offsetX = newOffset.coerceIn(-actionBoxWidthPx, 0f)
                        }
                    )
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.CenterStart) {
                Text("手动滑动逻辑 (更稳定)")
            }
        }
    }
}
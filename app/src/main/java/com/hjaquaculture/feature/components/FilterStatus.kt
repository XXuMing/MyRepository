package com.hjaquaculture.feature.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hjaquaculture.feature._temp.FilterGroup


data class FilterOption(
    val title: String,
    val items: List<String>,
    val selectedIndex: Int = 0,
    val onOptionSelected: (String) -> Unit
)


@Composable
fun StaggeredFilterColumn(
    visible: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(16.dp),
            content = content
        )
    }
}

@Composable
fun StaggeredItem(
    visible: Boolean,
    index: Int, // 根据索引计算延迟
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            // 每个元素比前一个多延迟 80ms
            animationSpec = tween(delayMillis = index * 80)
        ) + slideInVertically(
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            initialOffsetY = { -20 }
        )
    ) {
        content()
    }
}

@Preview
@Composable
fun AAA(){
    val filters = listOf("订单状态", "配送方式", "支付渠道", "时间范围")
    var isVisible: Boolean by remember { mutableStateOf(false) }

    Button({
        isVisible = !isVisible
    }) {
        Text("切换")
    }
    StaggeredFilterColumn(visible = isVisible) {
        filters.forEachIndexed { index, title ->
            StaggeredItem(visible = isVisible, index = index) {
                FilterGroup(title, listOf("全部", "未付", "已付"), "全部", {})
                if (index < filters.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
package com.hjaquaculture.feature._temp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf

// 模拟的 SaleOrder 和 SaleOrderItem 数据类
// 请替换为您项目中的真实数据类
data class SaleOrder(
    val id: String,
    val customerName: String,
    val totalAmount: Double,
    val status: String, // e.g., "待发货", "已完成", "已取消"
    val type: String,   // e.g., "线上订单", "线下开单"
    val createTime: String,
    val items: List<SaleOrderItem>
)

data class SaleOrderItem(
    val productName: String,
    val price: Double,
    val quantity: Int,
    val spec: String? // 规格
)

/**
 * 销售订单主页面 (屏幕)
 *
 * @param lazyPagingItems Paging库提供的分页数据项
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(
    // val lazyPagingItems = pagingDataFlow.collectAsLazyPagingItems()
) {
    // 模拟Paging数据流
    val mockOrders = List(20) { i ->
        SaleOrder(
            id = "SO20251231-00${i + 1}",
            customerName = "客户${i + 1}",
            totalAmount = 1288.50 + (i * 100),
            status = when (i % 3) {
                0 -> "已完成"
                1 -> "待发货"
                else -> "已取消"
            },
            type = if (i % 2 == 0) "线上订单" else "线下开单",
            createTime = "2025-12-31 10:30",
            items = listOf(
                SaleOrderItem("商品A", 299.0, 2, "红色, 500g"),
                SaleOrderItem("商品B", 345.25, 1, "大号")
            )
        )
    }
    val lazyPagingItems = flowOf(PagingData.from(mockOrders)).collectAsLazyPagingItems()

    var searchQuery by remember { mutableStateOf("") }
    // 其他过滤状态...

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("销售订单") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // 搜索与过滤面板
            SearchAndFilterPanel(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onTimeFilterClick = { /* TODO: 显示时间选择对话框 */ },
                onTypeFilterClick = { /* TODO: 显示类型选择对话框 */ },
                onStatusFilterClick = { /* TODO: 显示状态选择对话框 */ }
            )

            // 订单列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 在实际项目中，应使用 items(lazyPagingItems)
                items(lazyPagingItems.itemCount) { index ->
                    val order = lazyPagingItems[index]
                    order?.let {
                        SaleOrderCard(order = it)
                    }
                }

                // 可以根据Paging的加载状态显示加载中或错误提示
                // when (lazyPagingItems.loadState.append) { ... }
            }
        }
    }
}


/**
 * 订单信息卡片
 *
 * @param order 销售订单数据
 */
@Composable
fun SaleOrderCard(order: SaleOrder) {
    var isExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // --- 卡片头部 ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.customerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OrderStatusBadge(status = order.status)
            }

            Spacer(Modifier.height(8.dp))

            // --- 订单摘要信息 ---
            InfoRow(icon = Icons.AutoMirrored.Filled.ReceiptLong, text = "订单号: ${order.id}")
            InfoRow(icon = Icons.Default.Payments, text = "总金额: ¥${order.totalAmount}",
                contentColor = MaterialTheme.colorScheme.tertiary)
            InfoRow(icon = Icons.Default.CalendarToday, text = "下单时间: ${order.createTime}")

            // --- 展开/收起图标 ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "收起详情" else "查看详情",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // --- 可展开的订单项列表 ---
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
            ) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    Text(
                        "商品列表",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    order.items.forEach { item ->
                        SaleOrderItemView(item)
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

/**
 * 订单项 (单行商品) 视图
 */
@Preview
@Composable
fun SaleOrderItemViewPreview() {
    val mockItem = SaleOrderItem("商品A", 299.0, 2, "红色, 500g")
    SaleOrderItemView(mockItem)
}
@Composable
private fun SaleOrderItemView(item: SaleOrderItem) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.productName, fontWeight = FontWeight.SemiBold)
            if (item.spec != null) {
                Text(
                    "规格: ${item.spec}",
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.7f)
                )
            }
        }
        Text(
            "¥${item.price} x ${item.quantity}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

/**
 * 订单状态角标
 */
@Composable
fun OrderStatusBadge(status: String) {
    val (backgroundColor, contentColor) = when (status) {
        "已完成" -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        "待发货" -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        "已取消" -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = backgroundColor,
        shape = CircleShape,
    ) {
        Text(
            text = status,
            color = contentColor,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

/**
 * 带图标的信息行
 */
@Composable
private fun InfoRow(icon: ImageVector, text: String, contentColor: Color = LocalContentColor.current) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = contentColor.copy(alpha = 0.8f)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = contentColor)
    }
}


@Preview(showBackground = true)
@Composable
fun SalesScreenPreview() {
    MaterialTheme {
        SalesScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SaleOrderCardPreview() {
    val mockOrder = SaleOrder(
        id = "SO20251231-001",
        customerName = "预览客户",
        totalAmount = 1288.50,
        status = "待发货",
        type = "线上订单",
        createTime = "2025-12-31 10:30",
        items = listOf(
            SaleOrderItem("商品A", 299.0, 2, "红色, 500g"),
            SaleOrderItem("商品B", 345.25, 1, "大号")
        )
    )
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SaleOrderCard(order = mockOrder)
        }
    }
}

// 您项目中已有的 SearchAndFilterPanel Composable
@Composable
fun SearchAndFilterPanel(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onTimeFilterClick: () -> Unit,
    onTypeFilterClick: () -> Unit,
    onStatusFilterClick: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("搜索客户名称") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = false /* 根据实际状态绑定 */,
                onClick = onTimeFilterClick,
                label = { Text("订单时间") },
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
            )
            FilterChip(
                selected = false /* 根据实际状态绑定 */,
                onClick = onTypeFilterClick,
                label = { Text("订单类型") },
                leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) }
            )
            FilterChip(
                selected = false /* 根据实际状态绑定 */,
                onClick = onStatusFilterClick,
                label = { Text("订单状态") },
                leadingIcon = { Icon(Icons.Default.CheckCircleOutline, contentDescription = null) }
            )
        }
    }
}


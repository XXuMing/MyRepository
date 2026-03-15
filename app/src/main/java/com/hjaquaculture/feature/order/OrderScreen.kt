package com.hjaquaculture.feature.order

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun OrderScreen(
    vm: OrderViewModel = hiltViewModel(),
    scaffoldPadding: PaddingValues
){
    val listItems = vm.orderList.collectAsLazyPagingItems()
    val expandedItem by vm.expandedOrderItem.collectAsStateWithLifecycle()
    val detailState by vm.detailState.collectAsStateWithLifecycle()


    LazyColumn(
        modifier = Modifier.padding(scaffoldPadding)
    ) {
        items(
            count = listItems.itemCount,
            key = listItems.itemKey { it.syntheticId }
        ) { index ->
            val isExpanded = expandedItem == listItems[index]
            OrderCard(
                orderVO = listItems[index] ?: return@items,
                isExpanded = isExpanded,
                detailState = detailState,
                onToggle = { vm.toggleOrder(listItems[index] ?: return@OrderCard)}
            )
        }
    }

}
@Composable
fun OrderCard(
    orderVO: OrderVO,
    isExpanded: Boolean,            // 由外部控制是否展开
    detailState: OrderDetailState,       // 由外部传入当前的实时明细状态
    onToggle: () -> Unit            // 点击回调
) {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onToggle() }, // 触发外部状态改变
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize() // 展开时的平滑动画
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${orderVO.symbolDescription}: ${orderVO.partnerName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(orderVO.createdAt)
            }

            Row{
                Text("单号：${orderVO.sn}")
                Spacer(Modifier.weight(1f))
                Text(orderVO.orderStatus)
                Spacer(Modifier.weight(1f))
                Text(orderVO.creatorName)
                Spacer(Modifier.weight(1f))
            }

            Row{
                Text("数量：${orderVO.totalQuantity}")
                Spacer(Modifier.weight(1f))
                Text("备注：${orderVO.remark}")
                Spacer(Modifier.weight(1f))
            }

            Row{
                Text("预定日期：${orderVO.expiredAt}")
                Spacer(Modifier.weight(1f))
                Text("总金额：${orderVO.totalPrice}")
                Spacer(Modifier.weight(1f))

            }
            // --- 你的原始代码结束 ---

            // 【关键部分】：处理明细展示逻辑
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(), // 组合：淡入 + 垂直展开
                // exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
                exit = fadeOut() + shrinkVertically()  // 组合：淡出 + 垂直收缩
            ) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Column(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                    Box(modifier = Modifier.fillMaxWidth()) {

                        // 第一层：数据层（只要有数据就显示）
                        Column {
                            if (detailState.items.isNotEmpty()) {
                                detailState.items.forEach { item ->
                                    DetailItemRow(item)
                                }
                            } else if (!detailState.isLoading && !detailState.isIdle && detailState.error == null) {
                                // 只有在加载完成、没数据、没报错的情况下才显示“暂无”
                                Text("暂无明细数据", style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        // 第二层：加载层（叠加在数据层之上）
                        if (detailState.isLoading) {
                            // 如果 items 为空，显示大转圈；如果不为空，显示一个很小的加载标记（ERP风格）
                            if (detailState.items.isEmpty()) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp).align(Alignment.Center)
                                )
                            } else {
                                // 列表已有数据时的刷新感
                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth().height(2.dp).align(Alignment.TopCenter)
                                )
                            }
                        }

                        // 第三层：错误提示
                        if (detailState.error != null) {
                            Text(
                                text = "加载失败: ${detailState.error}",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItemRow(item: OrderItemVO) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item.productName, modifier = Modifier.weight(1f))
        Text("${item.quantity} x ¥${item.unitPrice / 100.0}", style = MaterialTheme.typography.bodyMedium)
        Text("= ¥${item.subtotal / 100.0}", fontWeight = FontWeight.SemiBold)
    }
}
package com.hjaquaculture.feature.invoice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.hjaquaculture.common.utils.InvoiceStatus
import com.hjaquaculture.common.utils.InvoiceSymbol
import com.hjaquaculture.common.utils.PaymentMethods
import com.hjaquaculture.feature.AuthAction
import com.hjaquaculture.feature.components.FilterGroupConfig
import com.hjaquaculture.feature.components.SearchFilterBar

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun InvoiceScreen(
    vm: InvoiceViewModel = hiltViewModel(),
    onAction: (AuthAction) -> Unit,
    scaffoldPadding: PaddingValues,
) {
    val listItems = vm.invoicePagingData.collectAsLazyPagingItems()
    val expandedItem by vm.expandedInvoiceItem.collectAsStateWithLifecycle()
    val detailState by vm.detailState.collectAsStateWithLifecycle()

    // ViewModel 中管理过滤状态（这里用本地 remember 演示）
    var searchQuery by remember { mutableStateOf("") }
    var selectedSymbol by remember { mutableStateOf<InvoiceSymbol?>(null) }
    var selectedStatus by remember { mutableStateOf<InvoiceStatus?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethods?>(null) }

    Column(modifier = Modifier.fillMaxWidth()
        .padding(scaffoldPadding)
        .padding(horizontal = 12.dp)
    )
    {

        SearchFilterBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            searchPlaceholder = "搜索账单号",
            filterGroups = listOf(
                FilterGroupConfig(
                    title = "账单类型",
                    options = InvoiceSymbol.entries,
                    selected = selectedSymbol,
                    onSelect = { selectedSymbol = it }
                ),
                FilterGroupConfig(
                    title = "付款方式",
                    options = PaymentMethods.entries,
                    selected = selectedPaymentMethod,
                    onSelect = { selectedPaymentMethod = it }
                ),
                FilterGroupConfig(
                    title = "账单状态",
                    options = InvoiceStatus.entries,
                    selected = selectedStatus,
                    onSelect = { selectedStatus = it }
                )
            )
        )

        LazyColumn {
            items(
                count = listItems.itemCount,
                key = listItems.itemKey { it.syntheticId }
            ) { index ->
                val item = listItems[index] ?: return@items
                val isExpanded = expandedItem == item

                Spacer(Modifier.height(8.dp))

                InvoiceCard(
                    invoiceVO = item,
                    isExpanded = isExpanded,
                    detailState = detailState,
                    onToggle = { vm.toggleInvoice(item) }
                )
            }
        }
    }
}

@Composable
fun InvoiceCard(
    invoiceVO: InvoiceVO,
    isExpanded: Boolean,
    detailState: InvoiceDetailState,
    onToggle: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 22.dp,
            focusedElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            // 头部
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${invoiceVO.symbol.description}: ${invoiceVO.partnerName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(invoiceVO.createdAt)
            }

            Row {
                Text("单号：${invoiceVO.sn}")
                Spacer(Modifier.weight(1f))
                Text(invoiceVO.status)
                Spacer(Modifier.weight(1f))
                Text(invoiceVO.creatorName)
                Spacer(Modifier.weight(1f))
            }
            Row {
                Text("应付：${invoiceVO.amountTotal}")
                Spacer(Modifier.weight(1f))
                Text("实付：${invoiceVO.amountPaid}")
                Spacer(Modifier.weight(1f))
                Text("欠付：${invoiceVO.amountRem}")
                Spacer(Modifier.weight(1f))
            }
            if (invoiceVO.remark != null) {
                Row {
                    Text("备注：${invoiceVO.remark}")
                    Spacer(Modifier.weight(1f))
                }
            }

            // 展开区域：流水明细
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(animationSpec = tween(150)) + expandVertically(animationSpec = tween(150)),
                exit = fadeOut(animationSpec = tween(120)) + shrinkVertically(animationSpec = tween(120))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // 数据列表
                        Column {
                            Text(
                                "付款流水",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            if (detailState.items.isEmpty() && !detailState.isLoading) {
                                Text(
                                    "暂无流水记录",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            } else {
                                detailState.items.forEach { item ->
                                    key(item.sn){
                                        PaymentItemRow(item)
                                    }
                                    Spacer(Modifier.height(4.dp))
                                }
                            }
                        }

                        // 加载指示
                        if (detailState.isLoading) {
                            if (detailState.items.isEmpty()) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp).align(Alignment.Center)
                                )
                            } else {
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .align(Alignment.TopCenter)
                                )
                            }
                        }

                        // 错误提示
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
fun PaymentItemRow(item: PaymentItemVO) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.sn, style = MaterialTheme.typography.bodySmall)
            Text(item.paymentTime, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline)
        }
        Text(item.paymentMethod, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.width(8.dp))
        Text(
            item.amount,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
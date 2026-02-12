package com.hjaquaculture.feature.invoice

import android.util.Log
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.hjaquaculture.feature._temp.FilterGroup
import com.hjaquaculture.feature._temp.FilterOption
import com.hjaquaculture.feature._temp.InlineFilterPanelStaggered

@Composable
fun SaleInvoiceScreen(
    viewModel: SaleInvoiceViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
){
    val listItems = viewModel.saleInvoicesList.collectAsLazyPagingItems()
    var filter by remember { mutableStateOf(FilterOption()) }

    var isFilterWrite by remember { mutableStateOf(false) }

    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){

            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth().weight(1f),
                label = { Text("搜索") },
                placeholder = { Text("商品类别/商品名称") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = MaterialTheme.shapes.medium
            )
            FilledIconButton(
                onClick = { isFilterWrite = !isFilterWrite },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (isFilterWrite) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(Icons.Default.Tune, contentDescription = "高级筛选")
            }
        }

        InlineFilterPanelStaggered(
            isVisible = isFilterWrite,
            currentOption = filter,
            onChanged = { filter = it }
        )
        LazyColumn {
            items(
                count = listItems.itemCount,
                key = listItems.itemKey { it.id }
            ) { index ->
                val item = listItems[index]
                item?.let { SaleInvoiceCard(it) }
                Spacer(Modifier.height(8.dp))
            }

        }
    }

}


@Preview
@Composable
fun SaleInvoiceCardPreview(){
    val invoiceSummary = SaleInvoiceSummaryVO(
        id = 1,
        sn = "123456",
        saleOrderId = 1,
        customerName = "客户1",
        creatorName = "张三",
        amountDue = "100",
        amountPaid = "50",
        amountRem = "50",
        status = "未付",
        createdAt = "2023-09-01",
        remark = "备注信息",
        isOrderSummaryExpanded = false,
        isPaymentSummaryExpanded = false,
    )
    SaleInvoiceCard(invoiceSummary)
}

@Composable
fun SaleInvoiceCard(invoice: SaleInvoiceSummaryVO ){
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
                    text = invoice.customerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(invoice.createdAt)
            }
            Row(){
                Text("单号：${invoice.sn}")
                Spacer(Modifier.weight(1f))
                Text(invoice.status)
                Spacer(Modifier.weight(1f))
                Text(invoice.creatorName)
                Spacer(Modifier.weight(1f))
            }
            Row{
                Text("应付：${invoice.amountDue}")
                Spacer(Modifier.weight(1f))
                Text("实付：${invoice.amountPaid}")
                Spacer(Modifier.weight(1f))
                Text("欠付：${invoice.amountRem}")
                Spacer(Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = if (isExpanded) "收起详情" else "查看详情",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
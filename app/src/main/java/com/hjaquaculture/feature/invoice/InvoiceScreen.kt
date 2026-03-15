package com.hjaquaculture.feature.invoice


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.hjaquaculture.common.utils.InvoiceSymbol
import com.hjaquaculture.feature.AuthAction
import com.hjaquaculture.feature._temp.FilterOption
import com.hjaquaculture.feature._temp.InlineFilterPanelStaggered

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InvoiceScreen(
    vm: InvoiceViewModel = hiltViewModel(),
    onAction: (AuthAction) -> Unit,
    scaffoldPadding: PaddingValues,
) {

    val listItems = vm.invoicePagingData.collectAsLazyPagingItems()
    var filter by remember { mutableStateOf(FilterOption()) }
    var isFilterWrite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(scaffoldPadding).padding(horizontal = 8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            OutlinedTextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
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
                key = listItems.itemKey { it.syntheticId }
            ) { index ->
                val item = listItems[index]
                item?.let { InvoiceCard(item) }
                Spacer(Modifier.height(8.dp))
            }

        }
    }



}


@Preview
@Composable
fun PreviewInvoiceCard() {
    InvoiceCard(
        InvoiceVO(
            symbol = InvoiceSymbol.SALE,
            symbolDescription = "销售订单",
            syntheticId = "SALE_INVOICE_1",
            originalId = 1,
            sn = "123456789",
            partnerId = 1,
            partnerName = "测试客户",
            creatorId = 1,
            creatorName = "测试用户",
            amountTotal = "100.00",
            amountPaid = "50.00",
            amountRem = "50.00",
            status = "已付",
            remark = "测试备注",
            createdAt = "2023-04-01",
            isDetailsExpanded = false
        )
    )
}
@Composable
fun InvoiceCard(invoiceVO: InvoiceVO){
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
                    text = "${invoiceVO.symbol.description}: ${invoiceVO.partnerName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(invoiceVO.createdAt)
            }
            Row{
                Text("单号：${invoiceVO.sn}")
                Spacer(Modifier.weight(1f))
                Text(invoiceVO.status)
                Spacer(Modifier.weight(1f))
                Text(invoiceVO.creatorName)
                Spacer(Modifier.weight(1f))
            }
            Row{
                Text("应付：${invoiceVO.amountTotal}")
                Spacer(Modifier.weight(1f))
                Text("实付：${invoiceVO.amountPaid}")
                Spacer(Modifier.weight(1f))
                Text("欠付：${invoiceVO.amountRem}")
                Spacer(Modifier.weight(1f))
            }
            if(invoiceVO.remark != null){
                Row{
                    Text("备注：${invoiceVO.remark}")
                    Spacer(Modifier.weight(1f))
                }
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
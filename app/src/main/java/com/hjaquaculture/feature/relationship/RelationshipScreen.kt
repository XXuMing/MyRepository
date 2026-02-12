package com.hjaquaculture.feature.relationship

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hjaquaculture.feature.invoice.PurchaseInvoiceScreen
import com.hjaquaculture.feature.invoice.SaleInvoiceScreen
import kotlinx.coroutines.launch

@Composable
fun RelationshipScreen(
    modifier: Modifier = Modifier
) {

    // 1. 定义 Tab 数据
    val titles = listOf("客户管理", "采购商管理")

    // 2. 创建 PagerState（控制页面滚动）
    // pageCount 需要与 Tab 数量一致
    val pagerState = rememberPagerState(pageCount = { titles.size })

    // 3. 创建协程作用域（用于点击 Tab 时执行滚动动画）
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // 4. 实现 PrimaryTabRow
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface, // 背景颜色
            contentColor = MaterialTheme.colorScheme.primary,   // 选中的文字/指示器颜色
            indicator = {
                // 默认指示器，也可以自定义
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(pagerState.currentPage),
                    width = 32.dp // 指示器宽度
                )
            }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        // 点击 Tab 时，同步滚动 Pager
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                )
            }
        }

        // 5. 实现内容区域 (HorizontalPager)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            when (pageIndex) {
                0 -> CustomerScreen()
                1 -> SupplierScreen()
            }
        }
    }
}
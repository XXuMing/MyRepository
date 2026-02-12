package com.hjaquaculture.feature.product

import android.R.attr.maxLines
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PriceManageScreen(
    modifier: Modifier = Modifier
){

    var showRangeModal by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf<Pair<Long?, Long?>>(null to null) }


    Button(
        onClick = {
            showRangeModal = !showRangeModal
        }
    ) {
        Text("Show Modal")
    }

    if (showRangeModal) {
        DateRangePickerModal(
            onDateRangeSelected = {
                selectedDateRange = it
                showRangeModal = false
            },
            onDismiss = { showRangeModal = false }
        )
    }


    DynamicComponentScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    // 日期格式化工具
    val formatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        // 关键属性：
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Row(modifier = Modifier.fillMaxWidth()){

                    // 这里的 title 是最上方的小字
                    Text(
                        text = "选择日期范围",
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                        style = MaterialTheme.typography.labelMedium // 使用较小的字体
                    )
                    // 快速回到今天的按钮
                    TextButton(
                        onClick = {
                            dateRangePickerState.displayedMonthMillis = System.currentTimeMillis()
                        }
                    ) {
                        Text("回到今天", style = MaterialTheme.typography.labelLarge)
                    }
                }
            },
            // --- 核心优化部分：自定义 Headline ---
            headline = {
                val start = dateRangePickerState.selectedStartDateMillis
                val end = dateRangePickerState.selectedEndDateMillis

                val displayText = if (start != null && end != null) {
                    "${formatter.format(Date(start))} - ${formatter.format(Date(end))}"
                } else if (start != null) {
                    "${formatter.format(Date(start))} - 结束日期"
                } else {
                    "选择日期"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = displayText,
                        // 稍微调小字号（默认是 24sp 左右，这里降到 18sp-20sp）
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 25.sp),
                        maxLines = 1
                    )
                }
            },
            showModeToggle = false,
            modifier = Modifier.height(500.dp)
        )
    }
}


//-------

@Composable
fun DynamicComponentScreen() {
    // 1. 定义状态：使用 mutableStateListOf 来存储组件的数据
    val items = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = {
            // 2. 点击时修改状态，添加一条新数据
            items.add("我是第 ${items.size + 1} 个组件")
        }) {
            Text("添加新组件")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. 根据状态渲染组件
        LazyColumn {
            items(items) { text ->
                MyDynamicCard(text)
            }
        }
    }
}

@Composable
fun MyDynamicCard(content: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(text = content, modifier = Modifier.padding(16.dp))
    }
}
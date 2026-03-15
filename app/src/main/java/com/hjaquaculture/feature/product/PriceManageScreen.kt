package com.hjaquaculture.feature.product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.hjaquaculture.feature.AuthAction
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@Composable
fun PriceManageScreen(
    onAction: (AuthAction) -> Unit,
    scaffoldPadding: PaddingValues,
){

    var showRangeModal by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf<Pair<Long?, Long?>>(null to null) }

    val n1 = System.currentTimeMillis()


    Column{

        Button(
            onClick = { showRangeModal = !showRangeModal }
        ) {
            Text("日期选择器")
        }

        MyDatePicker()
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

}

@Preview
@Composable
fun DateRangePickerModalPreview(){

    var showRangeModal by remember { mutableStateOf(false) }
    var selectedDateRange by remember { mutableStateOf<Pair<Long?, Long?>>(null to null) }

    DateRangePickerModal(
        onDateRangeSelected = {
            selectedDateRange = it
            showRangeModal = false
        },{})
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
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // “回到今天”：增加图标辅助识别
                TextButton(
                    onClick = { dateRangePickerState.displayedMonthMillis = System.currentTimeMillis() },
                    modifier = Modifier.height(48.dp) // 固定高度保证点击率
                ) {
                    Icon(Icons.Default.Today, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("回到今天")
                }

                // “指定明天”：辅助功能按钮
                OutlinedButton(
                    onClick = { /* 指定明天逻辑 */ },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                    shape = CircleShape,
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp)
                ) {
                    Text("指定明天", style = MaterialTheme.typography.labelLarge)
                }
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End // 右对齐确认组
            ) {
                // “取消”按钮：弱化处理
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.minimumInteractiveComponentSize() // 确保达到 48dp 触控标准
                ) {
                    Text("取消", color = MaterialTheme.colorScheme.outline)
                }

                Spacer(modifier = Modifier.width(32.dp))

                // “确定”按钮：强化处理（使用填充色按钮）
                Button(
                    onClick = { /* 确定逻辑 */ },
                    shape = RoundedCornerShape(12.dp), // 圆角更现代
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text("确定")
                }
            }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Row(modifier = Modifier.fillMaxWidth()){
                    // 这里的 title 是最上方的小字
                    Text(
                        text = "选择日期范围",
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp).fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium
                    )
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
                        maxLines = 1,
                        lineHeight = 40.sp,
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            showModeToggle = false,
            modifier = Modifier.height(500.dp)
        )
    }
}


@Composable
fun MyDatePicker() {
    // 1. 初始化状态，可以设置初始显示日期
    val datePickerState = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { showDialog = true }) {
            Text("选择日期")
        }

        // 选中的日期（时间戳格式）
        val selectedDate = datePickerState.selectedDateMillis?.let {
            // 简单处理时间戳转日期字符串
            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate().toString()
        } ?: "未选择"

        Text("当前选择: $selectedDate")
    }

    // 2. 弹窗逻辑
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("取消") }
            }
        ) {
            // 3. 将 DatePicker 放入 Dialog 中
            DatePicker(state = datePickerState)
        }
    }
}
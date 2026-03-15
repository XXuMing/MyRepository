package com.hjaquaculture.feature._temp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hjaquaculture.feature.MainViewModel

@Preview
@Composable
fun PurchaseScreenPreview(){
    PurchaseScreen(
        scaffoldPadding = PaddingValues(0.dp)
    )
}

@Composable
fun PurchaseScreen(
    viewModel: MainViewModel = hiltViewModel(),
    scaffoldPadding: PaddingValues
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            // safeDrawing 会自动避开：状态栏、刘海、以及底部的系统手势条
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(MaterialTheme.colorScheme.surface)
    ) {



        // 主内容
        Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
            Text("现在我不会被挡住了")
        }

        // 键盘直接贴边
        M3NumericKeyboard(onKeyClick = {})
    }
}

@Composable
fun M3NumericKeyboard(
    modifier: Modifier = Modifier,
    onKeyClick: (String) -> Unit
) {
    // 使用 Surface 作为底层，符合 M3 的高度（Elevation）和颜色规范
    Surface(
        modifier = modifier.fillMaxWidth().height(300.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RectangleShape,
        tonalElevation = 1.dp // M3 中通过色调提升来表现层次
    ) {
        // 使用自定义边框线颜色
        val borderColor = MaterialTheme.colorScheme.outlineVariant

        Column(
            Modifier.border(0.5.dp, borderColor)
        ) {
            // 第一行
            KeyboardRow(Modifier.weight(1f)) {
                KeyButton("7", Modifier.weight(1f), borderColor) { onKeyClick("1") }
                KeyButton("8", Modifier.weight(1f), borderColor) { onKeyClick("2") }
                KeyButton("9", Modifier.weight(1f), borderColor) { onKeyClick("3") }
                KeyButton("DEL", Modifier.weight(1f), borderColor) { onKeyClick("DELETE") }
            }
            // 第二行
            KeyboardRow(Modifier.weight(1f)) {
                KeyButton("4", Modifier.weight(1f), borderColor) { onKeyClick("4") }
                KeyButton("5", Modifier.weight(1f), borderColor) { onKeyClick("5") }
                KeyButton("6", Modifier.weight(1f), borderColor) { onKeyClick("6") }
                KeyButton(".", Modifier.weight(1f), borderColor) { onKeyClick(".") }
            }
            // 第三、四行组合区
            Row(Modifier.weight(2f)) {
                Column(Modifier.weight(3f)) {
                    KeyboardRow(Modifier.weight(1f)) {
                        KeyButton("1", Modifier.weight(1f), borderColor) { onKeyClick("7") }
                        KeyButton("2", Modifier.weight(1f), borderColor) { onKeyClick("8") }
                        KeyButton("3", Modifier.weight(1f), borderColor) { onKeyClick("9") }
                    }
                    KeyboardRow(Modifier.weight(1f)) {
                        KeyButton("←️", Modifier.weight(1f), borderColor) { onKeyClick("QTY") }
                        KeyButton("0", Modifier.weight(1f), borderColor) { onKeyClick("0") }
                        KeyButton("→", Modifier.weight(1f), borderColor) { onKeyClick("PRICE") }
                    }
                }
                // 右侧确定键：使用主色调(Primary)以符合 M3 强调动作的规范
                KeyButton(
                    text = "✔",
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    borderColor = borderColor,
                    isPrimary = true,
                    onClick = { onKeyClick("CONFIRM") }
                )
            }
        }
    }
}

@Composable
fun KeyboardRow(modifier: Modifier, content: @Composable RowScope.() -> Unit) {
    Row(modifier = modifier, content = content)
}

@Composable
fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    borderColor: Color,
    isPrimary: Boolean = false,
    onClick: () -> Unit
) {
    // M3 风格：如果点击动作很重要，背景色可以使用 Primary 或 SecondaryContainer
    val backgroundColor = if (isPrimary) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isPrimary) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(backgroundColor)
            .border(0.5.dp, borderColor, RectangleShape) // 细线边框
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = if (text.length > 1) 18.sp else 24.sp,
                textAlign = TextAlign.Center
            ),
            color = contentColor
        )
    }
}


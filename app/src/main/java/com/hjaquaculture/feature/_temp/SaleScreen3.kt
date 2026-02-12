package com.hjaquaculture.feature._temp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun SaleScreen3(){
    DialScreen()
}

@Composable
fun NumberKeyboard(
    onKeyClick: (String) -> Unit,
    onDelete: () -> Unit,
    onClear: () -> Unit
) {
    val keys = listOf("1", "2", "3", "4", "5", "6","7", "8", "9",".", "0", "√")

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 固定 3 列
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5)) // 键盘背景色
    ) {
        items(keys) { key ->
            KeyButton(key = key) {
                when (key) {
                    "单价" -> onClear()
                    "数量" -> onDelete()
                    else -> onKeyClick(key)
                }
            }
        }
    }
}

@Composable
fun KeyButton(key: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        tonalElevation = 2.dp,
        modifier = Modifier.aspectRatio(1.8f) // 控制按键长宽比
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = key,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (key == "单价" || key == "数量") Color.Red else Color.Black
            )
        }
    }
}

@Composable
fun DialScreen() {
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // 显示区域
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = input, fontSize = 32.sp, fontWeight = FontWeight.Light)
        }

        Box(Modifier.height(100.dp)){
            Text("20斤 * 20元 = 400元",
                fontSize = 30.sp, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().height(40.dp)
            )
        }
        // 键盘区域
        NumberKeyboard(
            onKeyClick = { digit -> if (input.length < 11) input += digit },
            onDelete = { if (input.isNotEmpty()) input = input.dropLast(1) },
            onClear = { input = "" }
        )
    }
}
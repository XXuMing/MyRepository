package com.hjaquaculture.feature.sale

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SaleScreen(
    modifier: Modifier = Modifier
){
    val windowSizeClass = calculateWindowSizeClass(LocalContext.current as Activity)
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    // 状态管理
    var selectedAId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedBId by rememberSaveable { mutableStateOf<String?>(null) }

    if (isExpanded) {
        // --- 大屏模式 (双面板) ---
        Row(Modifier.fillMaxSize()) {
            // 左侧面板：固定 Page A
            PageA(
                modifier = Modifier.weight(0.65f),
                onItemSelected = { id ->
                    selectedAId = id
                    selectedBId = null // 切换 A 时，重置 C，让右侧回到 B
                }
            )

            // 右侧面板：在 B 和 C 之间切换
            Box(Modifier.weight(0.35f).fillMaxHeight().background(MaterialTheme.colorScheme.surfaceVariant)) {
                if (selectedAId == null) {
                    Text("请选择项目", Modifier.align(Alignment.Center))
                } else if (selectedBId == null) {
                    // 显示 Page B
                    PageB(
                        parentId = selectedAId!!,
                        onItemSelected = { id -> selectedBId = id }
                    )
                } else {
                    // 显示 Page C
                    PageC(
                        parentId = selectedBId!!,
                        onBack = { selectedBId = null } // 返回 B
                    )
                }
            }
        }
    } else {
        // --- 小屏模式 (单面板) ---
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "pageA") {
            composable("pageA") {
                PageA(onItemSelected = { id ->
                    selectedAId = id
                    navController.navigate("pageB")
                })
            }
            composable("pageB") {
                PageB(parentId = selectedAId ?: "", onItemSelected = { id ->
                    selectedBId = id
                    navController.navigate("pageC")
                })
            }
            composable("pageC") {
                PageC(parentId = selectedBId ?: "", onBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}

@Composable
fun PageA(modifier: Modifier = Modifier, onItemSelected: (String) -> Unit) {
    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text("Page A (列表)", style = MaterialTheme.typography.headlineSmall)
        listOf("类别 1", "类别 2").forEach {
            Button(onClick = { onItemSelected(it) }) { Text(it) }
        }
    }
}

@Composable
fun PageB(parentId: String, onItemSelected: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Page B (属于 $parentId 的内容)", style = MaterialTheme.typography.headlineSmall)
        listOf("项 B-1", "项 B-2").forEach {
            Button(onClick = { onItemSelected(it) }) { Text(it) }
        }
    }
}

@Composable
fun PageC(parentId: String, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
        Text("Page C (详情: $parentId)", style = MaterialTheme.typography.headlineSmall)
        Text("这是最终的详情页面内容。")
    }
}
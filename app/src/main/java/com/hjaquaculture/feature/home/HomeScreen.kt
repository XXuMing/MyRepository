package com.hjaquaculture.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hjaquaculture.feature.AuthAction

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onAction = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAction: (AuthAction.Home) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Reload")
            }
        }
    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(100.dp)
            ) {
                Text(
                    text = "今日价格",
                    textAlign = TextAlign.Center,
                )
            }


            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(400.dp)
            ) {
                Text(
                    text = "销售流水",
                    textAlign = TextAlign.Center,
                )
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(400.dp)
            ) {
                Text(
                    text = "寄货 未付待处理",
                    textAlign = TextAlign.Center,
                )
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(400.dp)
            ) {
                Text(
                    text = "采购待支付",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}



/*

    // 观察当前页面的回退栈入口
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // 判断当前是否可以返回（如果栈里超过1个页面，就显示返回键）
    // 或者判断当前 Destimation 是否不等于 StartDestination
    val canNavigateBack = navController.previousBackStackEntry != null
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("欢迎使用") },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "返回"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
    }
* */
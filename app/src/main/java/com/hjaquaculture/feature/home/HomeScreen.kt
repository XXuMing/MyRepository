package com.hjaquaculture.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // 用于控制抽屉 (Drawer) 的状态
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {


    // 1. ModalNavigationDrawer 包含 Scaffold，用于侧边栏抽屉
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // 侧边栏的内容
            ModalDrawerSheet(
                modifier = Modifier.padding(end = 64.dp) // 确保内容有阴影和可见的边
            ) {
                Text("抽屉菜单项 1", modifier = Modifier.padding(16.dp))
                Text("抽屉菜单项 2", modifier = Modifier.padding(16.dp))
                Text("抽屉菜单项 3", modifier = Modifier.padding(16.dp))
                Text("抽屉菜单项 4", modifier = Modifier.padding(16.dp))
                Text("抽屉菜单项 5", modifier = Modifier.padding(16.dp))
                Text("抽屉菜单项 6", modifier = Modifier.padding(16.dp))
                Text("抽屉菜单项 7", modifier = Modifier.padding(16.dp))
                Text("抽屉菜单项 8", modifier = Modifier.padding(16.dp))
                Text("抽屉菜单项 9", modifier = Modifier.padding(16.dp))
                Text("抽屉菜单项 0", modifier = Modifier.padding(16.dp))
            }
        },
    ) {
        // 2. Scaffold 是整个屏幕布局的基础
        Scaffold(
            // 顶部应用栏
            topBar = {
                TopAppBar(
                    title = { Text("我的应用标题") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    navigationIcon = {
                        // 导航图标，点击打开抽屉
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "菜单"
                            )
                        }
                    },
                    actions = {
                        // 右侧操作图标
                        IconButton(onClick = { /* Handle action */ }) {
                            Icon(Icons.Filled.Settings, contentDescription = "设置")
                        }
                    }
                )
            },
            // 底部应用栏
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        "底部内容/导航",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            },
            // 悬浮操作按钮
            floatingActionButton = {
                FloatingActionButton(onClick = { /* Handle FAB click */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "添加")
                }
            }
        ) { paddingValues ->
            // 3. 内容区域
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // ⚠️ 必须应用这个 padding，否则内容会被 Top/Bottom Bar 遮盖
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "这是 Scaffold 的主体内容区域",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
    //}
}

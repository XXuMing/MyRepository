package com.hjaquaculture.feature._temp

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdaptiveScreen() {
    var currentNavDest by remember { mutableStateOf("list_route") }
    var selectedItemId by rememberSaveable { mutableStateOf<String?>(null) }

    // NavigationSuiteScaffold 自动处理：手机底栏 / 平板侧边栏
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                selected = currentNavDest == "list_route",
                onClick = { currentNavDest = "list_route" },
                icon = { Icon(Icons.AutoMirrored.Filled.List, null) },
                label = { Text("列表") }
            )
            item(
                selected = currentNavDest == "settings_route",
                onClick = { currentNavDest = "settings_route" },
                icon = { Icon(Icons.Default.Settings, null) },
                label = { Text("设置") }
            )
        }
    ) {

        val windowSizeClass = calculateWindowSizeClass(LocalContext.current as Activity)
        when (currentNavDest) {
            "list_route" -> {
                if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
                    // 大屏：双面板（无内部导航）
                    TwoPaneContent(selectedItemId) { selectedItemId = it }
                } else {
                    // 小屏：单面板（带动画导航）
                    OnePaneContent(selectedItemId) { selectedItemId = it }
                }
            }
            "settings_route" -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("设置页面")
            }
        }
    }
}

@Composable
fun OnePaneContent(selectedItemId: String?, onSelect: (String) -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ListScreen { id ->
                onSelect(id)
                navController.navigate("detail")
            }
        }
        composable(
            route = "detail",
            // 进入动画：从右往左进入
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(400))
            },
            // 返回动画：向右滑出屏幕 (重点修正)
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(400))
            },
            // 列表页在返回时的动画：从左往右进入
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(400))
            }
        ) {
            DetailScreen(itemId = selectedItemId ?: "", onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun TwoPaneContent(selectedItemId: String?, onSelect: (String) -> Unit) {
    Row(Modifier.fillMaxSize()) {
        ListScreen(Modifier.weight(0.4f), onSelect)
        Box(Modifier.weight(0.6f).fillMaxHeight().background(Color.LightGray), contentAlignment = Alignment.Center) {
            if (selectedItemId != null) DetailScreen(selectedItemId) else Text("请选择一项")
        }
    }
}

// --- 通用 UI 组件 ---

@Composable
fun ListScreen(modifier: Modifier = Modifier, onItemSelected: (String) -> Unit) {
    val items = listOf("项目 1", "项目 2", "项目 3")
    LazyColumn(modifier.fillMaxSize()) {
        items(items) { item ->
            ListItem(
                headlineContent = { Text(item) },
                modifier = Modifier.clickable { onItemSelected(item) }
            )
        }
    }
}

@Composable
fun DetailScreen(itemId: String, onBack: (() -> Unit)? = null) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (onBack != null) {
            Button(onClick = onBack) { Text("返回") }
        }
        Text("详情: $itemId", style = MaterialTheme.typography.headlineMedium)
        Text("这里是详细内容描述...")
    }
}
/*
import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// --- 顶部切换逻辑 ---
// 外部使用windowSizeClass.widthSizeClass，获取屏幕大小
@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdaptiveScreen() {
    // 1. 定义导航状态
    var currentDestination by remember { mutableStateOf("list") }
    // 2. 状态提升：管理选中的项目
    var selectedItemId by rememberSaveable { mutableStateOf<String?>(null) }

    // 核心组件：自动处理手机底栏和平板侧边栏
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            // 定义导航项（例如：首页、设置）
            item(
                selected = currentDestination == "list",
                onClick = { currentDestination = "list" },
                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                label = { Text("列表") }
            )
            item(
                selected = currentDestination == "settings",
                onClick = { currentDestination = "settings" },
                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                label = { Text("设置") }
            )
        }
    ) {
        // 3. 这里是内容区域
        // 我们利用 WindowSizeClass 来决定内容区是“单面板”还是“双面板”
        val windowSizeClass = calculateWindowSizeClass(LocalContext.current as Activity)

        when (currentDestination) {
            "list" -> {
                if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
                    // 平板：直接显示双面板，不需要 NavHost
                    TwoPaneLayout(
                        selectedItemId = selectedItemId,
                        onItemSelected = { selectedItemId = it }
                    )
                } else {
                    // 手机：使用 NavHost 处理切换动画
                    OnePaneLayout(
                        selectedItemId = selectedItemId,
                        onItemSelected = { selectedItemId = it }
                    )
                }
            }
            "settings" -> Text("设置页面")
        }
    }
}

// --- 双面板布局 (大屏) ---
@Composable
fun TwoPaneLayout(
    selectedItemId: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        // 主面板 (列表) - 占据 1/3 宽度
        ListScreen(
            onItemSelected = onItemSelected,
            modifier = Modifier.weight(0.5f)
        )

        // 详情面板 - 占据 2/3 宽度
        // 使用一个 Box 来确保即使没有选中项，详情区也可见
        Box(modifier = Modifier.weight(0.5f).fillMaxHeight().background(MaterialTheme.colorScheme.surfaceVariant)) {
            if (selectedItemId != null) {
                DetailScreen(itemId = selectedItemId)
            } else {
                Text(
                    "请从左侧列表中选择一个项目",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

// --- 单面板布局 (小屏) ---
@Composable
fun OnePaneLayout(
    selectedItemId: String?,
    onItemSelected: (String) -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list",
        modifier = Modifier.fillMaxSize()
    ) {
        val animationDuration = 300 // 动画时长 (毫秒)

        // --- 列表屏幕 (List Screen) ---
        composable(
            route = "list",
            // 进入时（从详情返回）：从左侧滑入 (popEnter)
            enterTransition = { slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End, // End 是左边
                tween(animationDuration)
            ) + fadeIn(tween(animationDuration)) },

            // 退出时（进入详情）：向左侧滑出 (exit)
            exitTransition = { slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, // Start 是右边
                tween(animationDuration)
            ) + fadeOut(tween(animationDuration)) },

            // popEnterTransition 和 popExitTransition 在这里定义的是“返回列表页”时的动画，
            // 它们和上面的 enter/exit 是镜像的。
            popEnterTransition = { fadeIn(tween(animationDuration)) }, // 通常返回列表时不需动画
            popExitTransition = { fadeOut(tween(animationDuration)) }
        ) {
            ListScreen(
                onItemSelected = { itemId ->
                    onItemSelected(itemId)
                    navController.navigate("detail")
                }
            )
        }

        // --- 详情屏幕 (Detail Screen) ---
        composable(
            route = "detail",
            // 进入时（从列表进入）：从右侧滑入 (enter)
            enterTransition = { slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, // Start 是右边
                tween(animationDuration)
            ) + fadeIn(tween(animationDuration)) },

            // 退出时（返回列表）：**向右侧滑出** (popExit)
            exitTransition = { slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End, // End 是左边
                tween(animationDuration)
            ) + fadeOut(tween(animationDuration)) },

            // popEnterTransition (列表页进入)
            // popExitTransition (详情页退出)

            // 返回时的进入动画：列表页从左侧滑入 (popEnter)
            popEnterTransition = { slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End, // End 是左边
                tween(animationDuration)
            ) + fadeIn(tween(animationDuration)) },

            // 返回时的退出动画：**详情页向右侧滑出** (popExit)
            popExitTransition = { slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start, // Start 是右边
                tween(animationDuration)
            ) + fadeOut(tween(animationDuration)) }
        ) {
            DetailScreen(
                itemId = selectedItemId ?: "N/A",
                onBack = { navController.popBackStack() }
            )
        }
    }
}
// --- 可重用 Composable 屏幕 ---

@Composable
fun ListScreen(
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemsList = listOf("项目 A", "项目 B", "项目 C", "项目 D", "项目 E")

    Column(modifier = modifier.fillMaxSize().padding(8.dp)) {
        Text("主列表", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(itemsList) { item ->
                ListItem(
                    headlineContent = { Text(item) },
                    modifier = Modifier.clickable { onItemSelected(item) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun DetailScreen(
    itemId: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null // 仅用于小屏
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (onBack != null) {
            Button(onClick = onBack) {
                Text("返回列表")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            "详情内容",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("当前选中的项目是: $itemId", style = MaterialTheme.typography.bodyLarge)
        Text(
            "这是关于 $itemId 的详细信息和描述...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
*/
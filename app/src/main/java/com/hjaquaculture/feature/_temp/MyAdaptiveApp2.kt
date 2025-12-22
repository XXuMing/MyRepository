package com.hjaquaculture.feature._temp

/*
class MyAdaptiveApp2 : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 获取屏幕尺寸类别
            val windowSizeClass = calculateWindowSizeClass(this)
            HJAquacultureTheme() {
                MyAdaptiveApp(windowSizeClass.widthSizeClass)

            }
        }
    }
}


// 路由定义
private object Destinations {
    const val MASTER_LIST = "masterList"
    const val DETAIL_SCREEN = "detailScreen/{itemId}"
    fun detailRoute(itemId: Int) = "detailScreen/$itemId"
}

/**
 * 整个应用程序的入口。根据屏幕宽度显示单面板或双面板布局。
 * @param widthSizeClass 屏幕宽度类别
 */
@Composable
fun MyAdaptiveApp(widthSizeClass: WindowWidthSizeClass) {
    // 假设的数据列表
    val items = List(10) { "Item $it" }

    // 在大屏（双面板）模式下，使用 mutableStateOf 来保存当前选中的详情
    var selectedItem: String? by remember { mutableStateOf(null) }

    val isExpanded = widthSizeClass != WindowWidthSizeClass.Compact // Compact: 手机小屏

    if (isExpanded) {
        // 双面板布局 (平板/大屏)
        TwoPaneLayout(
            items = items,
            onItemSelected = { itemId ->
                selectedItem = items.getOrNull(itemId)
            },
            selectedItem = selectedItem // 传递当前选中的详情
        )
    } else {
        // 单面板布局 (手机/小屏)
        SinglePaneNavHost(
            items = items
        )
    }
}

/**
 * 平板/大屏的双面板布局。
 */
@Composable
fun TwoPaneLayout(
    items: List<String>,
    onItemSelected: (Int) -> Unit,
    selectedItem: String?
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // 主面板 (Master)
        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            MasterListScreen(
                items = items,
                onItemSelected = onItemSelected
            )
        }

        // 详情面板 (Detail)
        VerticalDivider() // 可选的视觉分隔线

        Box(modifier = Modifier.weight(2f).fillMaxHeight()) {
            if (selectedItem != null) {
                DetailContentScreen(item = selectedItem)
            } else {
                // 未选择时显示提示
                Text(
                    text = "请从左侧选择一个项目",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

/**
 * 手机/小屏的单面板布局，使用 Navigation 实现页面切换。
 */
@Composable
fun SinglePaneNavHost(items: List<String>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.MASTER_LIST,

        // 要求2: 自定义动画 (右滑进入 / 左滑退出)
        // 进入动画：从屏幕右侧滑入 (类似 Activity 启动)
        enterTransition = {
            slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) // 100% 偏移
        },
        // 退出动画：滑动到屏幕右侧 (向前导航时的旧屏幕)
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth / 4 }) // 稍微往左滑出一点
        },
        // 弹回进入动画：从屏幕左侧滑入 (类似 Activity 返回)
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth / 4 }) // 稍微往右滑入一点
        },
        // 弹回退出动画：滑动到屏幕右侧 (返回时的当前屏幕)
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) // 100% 偏移
        }
    ) {
        // 主列表屏幕
        composable(Destinations.MASTER_LIST) {
            MasterListScreen(
                items = items,
                onItemSelected = { itemId ->
                    navController.navigate(Destinations.detailRoute(itemId))
                }
            )
        }

        // 详情屏幕
        composable(Destinations.DETAIL_SCREEN) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()
            val item = items.getOrNull(itemId ?: -1)

            if (item != null) {
                DetailContentScreen(item = item)
            } else {
                Text("项目未找到", modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
            }
        }
    }
}

/**
 * 主列表内容 Composable。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterListScreen(
    items: List<String>,
    onItemSelected: (Int) -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("主列表") }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(items) { index, item ->
                ListItem(
                    modifier = Modifier.clickable { onItemSelected(index) },
                    headlineContent = { Text(item) },
                    supportingContent = { Text("点击查看详情") },
                )
                HorizontalDivider()
            }
        }
    }
}

/**
 * 详情内容 Composable。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContentScreen(item: String) {
    Scaffold(topBar = { TopAppBar(title = { Text("详情") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "这是 $item 的详细内容。", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

 */
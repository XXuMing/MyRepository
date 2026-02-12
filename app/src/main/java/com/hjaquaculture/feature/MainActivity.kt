package com.hjaquaculture.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hjaquaculture.common.utils.toScreen
import com.hjaquaculture.feature.AuthAction.Global
import com.hjaquaculture.ui.theme.HJAquacultureTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 单Activity
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HJAquacultureTheme {
                AppNavigation()
            }
        }
    }
}

// 定义一个管理全局动作的类
class GlobalActionsManager {
    var onBackClick: (() -> Unit)? = null
}
/**
 * 宿主层 (AppShell)
 * 它负责持有 NavController 和全局 UI 状态（如当前路由、BottomBar 是否显示）。
 * Scaffold 放在这里：它作为整个应用的“壳”。
 * TopBar / BottomBar：根据当前路由状态动态控制显示内容。
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel()
) {

    val navController = rememberNavController() // 唯一真理源

    // 监听当前路由状态
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination


    // 逻辑：如果当前路由不是登录/注册，则显示 Scaffold
    val showScaffold = destination?.let {
        !it.hasRoute<Screen.Login>() && !it.hasRoute<Screen.Register>()
    } ?: false // 默认不显示，直到确定位置

    // 1. 状态管理：控制抽屉的打开与关闭
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 当前选中的项目索引
    var selectedItem by remember { mutableIntStateOf(0) }

    val navigationItems = listOf(
        NavigationItem("首页", Icons.Default.Home,authAction = Global.Home),
        NavigationItem("销售", Icons.Default.Email,Global.Sale),
        NavigationItem("采购", Icons.Default.Favorite,Global.Purchase),
        NavigationItem("销售账单", Icons.Default.Settings,Global.SaleInvoice),
        NavigationItem("采购账单", Icons.Default.Settings,Global.PurchaseInvoice),
        NavigationItem("价格管理", Icons.Default.Settings,Global.PriceManage),
        NavigationItem("商品管理", Icons.Default.Settings,Global.ProductManage),
        NavigationItem("关系管理", Icons.Default.Settings,Global.RelationshipManage)
    )

    ModalNavigationDrawer(
        gesturesEnabled = showScaffold,
        drawerState = drawerState,
        // 2. 抽屉的具体内容
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp), // 圆角修饰
                drawerContainerColor = MaterialTheme.colorScheme.surface,
            ) {
                // --- 抽屉头部 (Header) ---
                DrawerHeader()

                Spacer(Modifier.height(12.dp))

                // --- 导航列表 (Items) ---
                navigationItems.forEachIndexed { index, item ->
                    if(index == 3){
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Spacer(Modifier.height(12.dp))
                    }
                    NavigationDrawerItem(
                        label = { Text(text = item.title, fontWeight = FontWeight.Medium) },
                        selected = index == selectedItem,
                        onClick = {
                            selectedItem = index
                            // 获取目标 Screen
                            val targetScreen = item.authAction.toScreen()
                            //判断当前页面与目标页面不一致才执行跳转
                            if (destination?.hasRoute(targetScreen::class) == false){
                                when(item.authAction) {
                                    is Global.Back -> {
                                        navController.popBackStack()
                                    }
                                    is Global.Reload -> {
                                        // ------------------
                                    }
                                    is Global.Home -> {
                                        navController.navigateWithPopUp(Screen.Home)
                                    }
                                    is Global.Sale -> {
                                        navController.navigateWithPopUp(Screen.Sale)
                                    }
                                    is Global.Purchase -> {
                                        navController.navigateWithPopUp(Screen.Purchase)
                                    }
                                    is Global.SaleInvoice -> {
                                        navController.navigateWithPopUp(Screen.SaleInvoice)
                                    }
                                    is Global.PurchaseInvoice -> {
                                        navController.navigateWithPopUp(Screen.PurchaseInvoice)
                                    }
                                    is Global.PriceManage -> {
                                        navController.navigateWithPopUp(Screen.PriceManage)
                                    }
                                    is Global.ProductManage -> {
                                        navController.navigateWithPopUp(Screen.Product)
                                    }
                                    is Global.RelationshipManage -> {
                                        navController.navigateWithPopUp(Screen.Relationship)
                                    }
                                    is Global.Setting -> {
                                        navController.navigateWithPopUp(Screen.Setting)
                                    }
                                }
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        // 自定义选中颜色
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    ) {
        // 3. 主屏幕内容
        Scaffold(
            topBar = {
                if(showScaffold){
                    CenterAlignedTopAppBar(
                        title = { Text("首页") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            // 右侧操作图标
                            IconButton(onClick = { /* Handle action */ }) {
                                Icon(painterResource(com.hjaquaculture.R.drawable.paid_24px), contentDescription = "价格")
                            }
                            IconButton(onClick = { /* Handle action */ }) {
                                Icon(Icons.Filled.Settings, contentDescription = "今日价格")
                            }
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "返回"
                                )
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavGraph(
                navController=navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}


@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("User", color = MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(Modifier.height(12.dp))
        Text("Jetpack Compose",fontWeight = FontWeight.Bold)
        Text("example@email.com" )
    }
}

data class NavigationItem(val title: String, val icon: ImageVector,val authAction: Global)

@Composable
fun SaleInvoiceCard(){

}
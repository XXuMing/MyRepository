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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hjaquaculture.R
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


data class NavigationItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val group: String,
    val action: MenuEvent
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    // 逻辑：如果当前路由不是登录/注册，则显示 Scaffold
    val showScaffold = destination?.let {
        !it.hasRoute<ScreenPage.Login>() && !it.hasRoute<ScreenPage.Register>()
    } ?: false

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("home") }

    val navigationItems = listOf(
        NavigationItem(id ="home", title = "首页", Icons.Default.Home, action = MenuEvent.ToHome, group = "主菜单"),
        NavigationItem(id ="product", title = "商品", Icons.Default.ShoppingBag, action = MenuEvent.ToProduct, group = "主菜单"),
        NavigationItem(id ="sale", title = "订单", Icons.Default.Receipt, action = MenuEvent.ToOrder, group = "管理"),
        NavigationItem(id ="invoice", title = "账单",Icons.AutoMirrored.Filled.ReceiptLong, action = MenuEvent.ToInvoice, group = "管理"),
        NavigationItem(id ="relationship", title = "关系", Icons.Default.Person, action = MenuEvent.ToRelationship, group = "管理"),
        NavigationItem(id ="setting", title = "设置", Icons.Default.Settings, action = MenuEvent.ToSetting, group = "系统")
    )

    val groupedItems = navigationItems.groupBy { it.group }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showScaffold,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(320.dp),
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
            ) {
                DrawerHeader()

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    groupedItems.forEach { (groupName, items) ->
                        HorizontalDivider(modifier = Modifier.padding(vertical = 1.dp))
                        Text(
                            text = groupName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                        )
                        items.forEach { item ->
                            NavigationDrawerItem(
                                label = { Text(item.title) },
                                icon = { Icon(item.icon, null) },
                                selected = selectedItem == item.id,
                                onClick = {
                                    selectedItem = item.id
                                    scope.launch {
                                        drawerState.close()
                                        RouteLogic(navController).handleMenuAction(item.action)
                                    }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    unselectedContainerColor = Color.Transparent
                                ),
                                modifier = Modifier.padding(vertical = 2.dp),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if(showScaffold){
                    CenterAlignedTopAppBar(
                        title = {
                            val title = when {
                                destination.hasRoute<ScreenPage.Home>() -> "首页"
                                destination.hasRoute<ScreenPage.Order>() -> "订单"
                                destination.hasRoute<ScreenPage.Product>() -> "商品"
                                destination.hasRoute<ScreenPage.Invoice>() -> "账单"
                                destination.hasRoute<ScreenPage.Relationship>() -> "关系管理"
                                destination.hasRoute<ScreenPage.Setting>() -> "设置"
                                else -> "应用"
                            }
                            Text(text = title)
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                onClick = {}
                            ) {
                                Icon(painterResource(R.drawable.currency_yen_24px), "价格")
                            }
                            IconButton(
                                modifier = Modifier.padding(start = 4.dp, end = 8.dp).width(60.dp),
                                onClick = {
                                    if(navController.previousBackStackEntry != null){
                                        navController.navigateUp()
                                    }
                                }


                            ) {
                                Icon(painterResource(R.drawable.undo_24px), "返回")
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavGraph(navController = navController, scaffoldPadding = innerPadding)
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
        Text("Jetpack Compose", fontWeight = FontWeight.Bold)
        Text("example@email.com", fontSize = 12.sp, color = Color.Gray)
    }
}
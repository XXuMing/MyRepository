package com.hjaquaculture.feature

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.hjaquaculture.feature.ScreenPage.Home
import com.hjaquaculture.feature.ScreenPage.Invoice
import com.hjaquaculture.feature.ScreenPage.Login
import com.hjaquaculture.feature.ScreenPage.Order
import com.hjaquaculture.feature.ScreenPage.OrderManagementScreen
import com.hjaquaculture.feature.ScreenPage.Product
import com.hjaquaculture.feature.ScreenPage.Register
import com.hjaquaculture.feature.ScreenPage.Relationship
import com.hjaquaculture.feature.ScreenPage.Setting

// --- 路由跳转工具 开始 ---
/**
 * 导航扩展工具类
 */

// 1. 全局单实例模式 (主要用于侧边栏、BottomBar)
// 逻辑：弹出到起始页以上的所有页面，并复用目标页面。
// 栈变化：[Home, A, B] -> navigate -> [Home, Target]
fun NavController.navigateAndCleanStack(route: Any) {
    this.navigate(route) {
        // 弹出到导航图的起始目的地 (通常是 Home)
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // 避免在栈顶重复创建同一个目标页面
        launchSingleTop = true
        // 恢复之前在目标页面保存的状态（如滚动位置）
        restoreState = true
    }
}

// 2. 替换当前页模式 (主要用于：登录成功跳转首页、支付成功跳结果页)
// 逻辑：跳转到新页面的同时，把当前页面从栈里杀掉。
// 栈变化：[Home, Login] -> navigate -> [Home, Main] (按返回键不会回到 Login)
fun NavController.navigateAndReplace(route: Any) {
    val currentRoute = this.currentBackStackEntry?.destination?.route
    this.navigate(route) {
        if (currentRoute != null) {
            popUpTo(currentRoute) {
                inclusive = true // 包含当前页，一并弹出
            }
        }
    }
}

// 3. 线性跳转模式 (主要用于：详情页 -> 更多详情)
// 逻辑：标准的压栈，但开启 singleTop 防止用户由于手抖“连点”产生的重复页面。
// 栈变化：[A] -> navigate -> [A, B]
fun NavController.navigateSingleTop(route: Any) {
    this.navigate(route) {
        launchSingleTop = true
    }
}

// 4. 清理特定路由模式 (主要用于：解决 A-B-A-B 循环)
// 逻辑：跳转到目标页时，如果栈里已经有这个页面，则把它上方的所有页面全部清空。
// 栈变化：[Home, A, B, C] -> navigateToExist(A) -> [Home, A]
fun NavController.navigateToExist(route: Any) {
    this.navigate(route) {
        // 弹出到目标页面，inclusive = false 表示不销毁目标页本身，而是回到它
        popUpTo(route) {
            inclusive = false
        }
        launchSingleTop = true
    }
}

// --- 路由跳转工具 结束 ---


/**
 * 路由逻辑：处理跳转逻辑
 */
class RouteLogic(private val navController: NavController) {

    /**
     * 处理登录页发出的动作
      */
    fun handleAuthAction(action: AuthAction) {
        when (action) {
            is AuthAction.GoToRegister -> {
                navController.navigate(Register)
            }
            is AuthAction.LoginSuccess -> {
                // 登录成功，跳转到 Home，并清空登录栈
                navController.navigate(Home){
                    popUpTo<Login> { inclusive = true }
                }
            }

            is AuthAction.BackToLogin -> {
                navController.popBackStack()
            }
            is AuthAction.RegisterSuccess -> {
                // 类型安全地传递参数
                navController.navigate(Login(initialUsername = action.account)) {
                    // 核心逻辑：将注册页从栈中弹出
                    popUpTo<Register> { inclusive = true }
                    // 确保如果已经在登录页，不会重复创建实例
                    launchSingleTop = true
                }
            }

            is AuthAction.OrderManagementScreen -> {
                navController.navigate(OrderManagementScreen)
            }
        }
    }

    /**
     * 处理侧边栏发出的动作
     */
    fun handleMenuAction(action: MenuEvent) {
        when(action){
            MenuEvent.ToHome -> navController.navigateAndCleanStack(Home)
            MenuEvent.ToInvoice -> navController.navigateAndCleanStack(Invoice)
            MenuEvent.ToOrder -> navController.navigateAndCleanStack(Order)
            MenuEvent.ToProduct -> navController.navigateAndCleanStack(Product)
            MenuEvent.ToRelationship -> navController.navigateAndCleanStack(Relationship)
            MenuEvent.ToSetting -> navController.navigateAndCleanStack(Setting)
        }
    }

}

package com.hjaquaculture.feature

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

/**
 * 自定义跳转扩展函数
 */
fun NavController.navigateWithPopUp(route: Any) {
    val startRoute = graph.findStartDestination().route
    this.navigate(route) {
        // 弹出到起始站点，避免堆栈堆积
        if(startRoute != null){
            popUpTo(startRoute) {
                saveState = true
            }
        }
        // 开启 SingleTop 模式
        launchSingleTop = true
        // 恢复之前保存的状态
        restoreState = true
    }

}


/**
 * 路由逻辑：处理跳转逻辑
 */
class RouteLogic(private val navController: NavController) {

    fun handleTestAction(action: AuthAction.Test){

        when(action){
            is AuthAction.Test.Home -> {
                navController.navigate(Screen.Home)
            }
            is AuthAction.Test.TempSale -> {
                navController.navigate(Screen.Test.Sale)
            }
            is AuthAction.Test.Filter01 -> {
                navController.navigate(Screen.Test.Filter01)
            }
            is AuthAction.Test.SwipeToReveal01 -> {
                navController.navigate(Screen.Test.SwipeToReveal01)
            }
            is AuthAction.Test.SwipeToReveal02 -> {
                navController.navigate(Screen.Test.SwipeToReveal02)
            }
            is AuthAction.Test.AdaptiveApp -> {
                navController.navigate(Screen.Test.AdaptiveApp)
            }
            is AuthAction.Test.ThreePaneAdaptiveApp -> {
                navController.navigate(Screen.Test.ThreePaneAdaptiveApp)
            }
            is AuthAction.Test.MyAdaptiveApp2 -> {
                navController.navigate(Screen.Test.MyAdaptiveApp2)
            }
            is AuthAction.Test.Sale -> {
                navController.navigate(Screen.Sale)
            }
            is AuthAction.Test.Sale3 -> {
                navController.navigate(Screen.Test.Sale3)
            }
        }
    }
    /**
     * 处理登录页发出的动作
      */
    fun handleLoginAction(action: AuthAction.Login) {
        when (action) {
            is AuthAction.Login.GoToRegister -> {
                navController.navigate(Screen.Register)
            }
            is AuthAction.Login.LoginSuccess -> {
                // 登录成功，跳转到 Home，并清空登录栈
                navController.navigate(Screen.Home){
                    popUpTo<Screen.Login> { inclusive = true }
                }
            }
            is AuthAction.Login.GotoTestScreen -> {
                navController.navigate(Screen.Test.Home)
            }
        }
    }

    /**
     * 处理注册页发出的动作
     */
    fun handleRegisterAction(action: AuthAction.Register) {
        when (action) {
            is AuthAction.Register.RegisterSuccess -> {
                // 类型安全地传递参数
                navController.navigate(Screen.Login(initialUsername = action.account)) {
                    // 核心逻辑：将注册页从栈中弹出
                    popUpTo<Screen.Register> { inclusive = true }
                    // 确保如果已经在登录页，不会重复创建实例
                    launchSingleTop = true
                }
            }
            is AuthAction.Register.BackToLogin -> {
                navController.popBackStack()
            }
        }
    }

    fun handleHomeAction(action: AuthAction.Home) {
        when(action){
            is AuthAction.Home.CheckPriceManage -> {}
            is AuthAction.Home.CheckSalePayment -> {}
            is AuthAction.Home.CheckShippingUnpaidPending -> {}
            is AuthAction.Home.CheckPurchaseUnpaidPending -> {}
        }
    }
}

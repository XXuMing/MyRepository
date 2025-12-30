package com.hjaquaculture.feature

import androidx.navigation.NavController

class ScreenRouteLogic(private val navController: NavController) {

    // 处理登录页发出的动作
    fun handleLoginAction(action: LoginAction) {
        when (action) {
            is LoginAction.GoToRegister -> {
                navController.navigate(RegisterRoute)
            }
            is LoginAction.LoginSuccess -> {
                navController.navigate(ThreePaneAdaptiveAppRoute)
            }
        }
    }

    // 处理注册页发出的动作
    fun handleRegisterAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.RegisterSuccess -> {
                // 类型安全地传递参数
                navController.navigate(LoginRoute(initialUsername = action.username)) {
                    // 核心逻辑：将注册页从栈中弹出
                    popUpTo<RegisterRoute> { inclusive = true }
                    // 确保如果已经在登录页，不会重复创建实例
                    launchSingleTop = true
                }
            }
            is RegisterAction.BackToLogin -> {
                navController.popBackStack()
            }
        }
    }
}

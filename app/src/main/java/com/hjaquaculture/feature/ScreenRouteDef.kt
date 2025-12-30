package com.hjaquaculture.feature

import kotlinx.serialization.Serializable

// Screen 路由配置

@Serializable
data class LoginRoute(
    // 设定默认值就表示为可选参数。
    val initialUsername: String? = null
)
sealed class LoginAction {
    object LoginSuccess : LoginAction()
    object GoToRegister : LoginAction()
    // 如果将来有带参数跳转，也可以这样写：
    // data class GoToDetails(val id: String) : LoginAction()
}

@Serializable
object RegisterRoute
sealed class RegisterAction {
    // 注册成功，携带用户名用于自动填充登录框
    data class RegisterSuccess(val username: String) : RegisterAction()
    // 返回登录（不带参数，用户手动点击返回）
    object BackToLogin : RegisterAction()
}

@Serializable
object HomeRoute

@Serializable
object AdaptiveAppRoute

@Serializable
object ThreePaneAdaptiveAppRoute


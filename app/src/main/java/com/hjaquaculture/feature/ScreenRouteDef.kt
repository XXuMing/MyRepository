package com.hjaquaculture.feature

import kotlinx.serialization.Serializable

// Screen 路由配置

/**
 * 登录页面的路由参数
 * @property initialUsername 初始用户名
 */
@Serializable
data class LoginRoute(
    // 给定一个默认值则表示此参数为可选参数。
    val initialUsername: String? = null
)

/**
 * 登录页面的行为
 */
sealed class LoginAction {
    /**
     *
     */
    object LoginSuccess : LoginAction()
    /**
     *
     */
    object GoToRegister : LoginAction()
    // 如果将来有带参数跳转，也可以这样写：
    // data class GoToDetails(val id: String) : LoginAction()
}

/**
 * 注册页面的路由参数
 */
@Serializable
object RegisterRoute

/**
 * 注册页面的行为
 */
sealed class RegisterAction {
    /**
     * 注册成功，携带用户名用于自动填充登录框
     * @property account 用户名
     */
    data class RegisterSuccess(val account: String) : RegisterAction()

    /**
     * 返回登录（不带参数，用户手动点击返回）
     */
    object BackToLogin : RegisterAction()
}


@Serializable
object HomeRoute

@Serializable
object AdaptiveAppRoute

@Serializable
object ThreePaneAdaptiveAppRoute

@Serializable
object TempSaleScreenRoute


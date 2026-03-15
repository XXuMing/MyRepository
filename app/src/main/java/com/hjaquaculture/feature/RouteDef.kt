package com.hjaquaculture.feature

import kotlinx.serialization.Serializable

// -----------
// ScreenPage 路由配置
// -----------

@Serializable
sealed interface ScreenPage {

    /**
     * 登录页面的路由参数
     * @property initialUsername 初始用户名
     */
    @Serializable
    data class Login(
        // 给定一个默认值则表示此参数为可选参数。
        val initialUsername: String? = null
    ) : ScreenPage

    /**
     * 注册页面的路由参数
     */
    @Serializable
    data object Register: ScreenPage

    @Serializable
    data object Home: ScreenPage

    @Serializable
    data object Order: ScreenPage

    @Serializable
    data object OrderManagementScreen: ScreenPage

    @Serializable
    data object Product: ScreenPage

    @Serializable
    data object Invoice: ScreenPage

    @Serializable
    data object Relationship: ScreenPage

    @Serializable
    data object Setting: ScreenPage

}


// -----------
// Action 行为
// -----------

// 1. 全局基础动作
sealed interface GlobalAction {
    data object Back : GlobalAction
    data object Reload : GlobalAction
}

sealed interface MenuEvent{
    data object ToHome: MenuEvent
    data object ToOrder: MenuEvent
    data object ToProduct: MenuEvent
    data object ToInvoice: MenuEvent
    data object ToRelationship: MenuEvent
    data object ToSetting: MenuEvent
}

// 2. 业务特定的 UI 事件 (由 ViewModel 发出，Compose 监听)
sealed interface AuthAction {

    data object LoginSuccess : AuthAction

    data object GoToRegister : AuthAction

    data class RegisterSuccess(val account: String) : AuthAction

    data object BackToLogin :AuthAction

    data object OrderManagementScreen : AuthAction
}
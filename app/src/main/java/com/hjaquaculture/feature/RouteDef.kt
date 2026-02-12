package com.hjaquaculture.feature

import kotlinx.serialization.Serializable

// -----------
// Screen 路由配置
// -----------

@Serializable
sealed interface Screen {
    //------- 测试部分开始

    @Serializable
    sealed interface Test: Screen{
        @Serializable
        data object Home : Test
        @Serializable
        data object AdaptiveApp : Test
        @Serializable
        data object ThreePaneAdaptiveApp : Test
        @Serializable
        data object Sale : Test
        @Serializable
        data object Sale3 : Test
        @Serializable
        data object MyAdaptiveApp2 : Test
        @Serializable
        data object SwipeToReveal01 : Test
        @Serializable
        data object SwipeToReveal02 : Test
        @Serializable
        data object Filter01 : Test
    }
    // --------- 测试部分结束

    /**
     * 登录页面的路由参数
     * @property initialUsername 初始用户名
     */
    @Serializable
    data class Login(
        // 给定一个默认值则表示此参数为可选参数。
        val initialUsername: String? = null
    ) : Screen

    /**
     * 注册页面的路由参数
     */
    @Serializable
    data object Register : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Relationship : Screen

    @Serializable
    data object Product : Screen

    @Serializable
    data object SaleInvoice : Screen

    @Serializable
    data object PurchaseInvoice : Screen


    @Serializable
    data object Sale : Screen

    @Serializable
    data object Purchase : Screen

    @Serializable
    data object PriceManage : Screen

    @Serializable
    data object Setting : Screen




}


// -----------
// Action 行为
// -----------

sealed interface AuthAction {

    /**
     * 测试页跳转
     */
    sealed interface Test{
        data object Home : Test
        data object TempSale : Test
        data object Filter01 : Test
        data object SwipeToReveal01 : Test
        data object SwipeToReveal02 : Test
        data object MyAdaptiveApp2 : Test
        data object Sale : Test
        data object Sale3 : Test
        data object AdaptiveApp : Test
        data object ThreePaneAdaptiveApp : Test

    }

    sealed interface Global{
        data object Back : Global
        data object Reload : Global
        data object Home : Global
        data object Sale : Global
        data object Purchase : Global
        //data object InvoiceManage : Global
        data object SaleInvoice : Global
        data object PurchaseInvoice : Global
        data object PriceManage : Global
        data object ProductManage : Global
        data object RelationshipManage : Global
        data object Setting : Global
    }
    // 登录页面的行为
    sealed interface Login {
        /**
         * 登录成功
         */
        data object LoginSuccess : Login

        /**
         * 跳转到注册页
         * */
        data object GoToRegister : Login
        // 如果将来有带参数跳转，也可以这样写：
        // data class GoToDetails(val id: String) : Login

        /**
         * 跳转到测试页面
         */
        data object GotoTestScreen : Login
    }

    // 注册页面的行为
    sealed interface Register {
        /**
         * 注册成功，携带用户名用于自动填充登录框
         * @property account 用户名
         */
        data class RegisterSuccess(val account: String) : Register
        /**
         * 返回登录（不带参数，用户手动点击返回）
         */
        data object BackToLogin : Register
    }

    sealed interface Home {
        /**
         * 查看今日价格
         */
        data object CheckPriceManage : Home

        /**
         * 查看销售流水
         */
        data object CheckSalePayment : Home

        /**
         * 查看销售待支付
         */
        data object CheckShippingUnpaidPending : Home

        /**
         * 查看采购待支付
         */
        data object CheckPurchaseUnpaidPending : Home
    }
}



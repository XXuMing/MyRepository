package com.hjaquaculture.feature

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hjaquaculture.common.utils.NavAnimations
import com.hjaquaculture.feature._temp.AdaptiveScreen
import com.hjaquaculture.feature._temp.OrderFilterScreen
import com.hjaquaculture.feature._temp.SaleScreen3
import com.hjaquaculture.feature._temp.SalesScreen
import com.hjaquaculture.feature._temp.SwipeToReveal_01
import com.hjaquaculture.feature._temp.SwipeToReveal_02
import com.hjaquaculture.feature._temp.ThreePaneAdaptiveApp
import com.hjaquaculture.feature.account.LoginScreen
import com.hjaquaculture.feature.account.LoginViewModel
import com.hjaquaculture.feature.account.RegisterScreen
import com.hjaquaculture.feature.home.HomeScreen
import com.hjaquaculture.feature.invoice.InvoiceScreen
import com.hjaquaculture.feature.invoice.PurchaseInvoiceScreen
import com.hjaquaculture.feature.invoice.SaleInvoiceScreen
import com.hjaquaculture.feature.product.PriceManageScreen
import com.hjaquaculture.feature.product.ProductScreen
import com.hjaquaculture.feature.purchase.PurchaseScreen
import com.hjaquaculture.feature.relationship.CustomerScreen
import com.hjaquaculture.feature.relationship.RelationshipScreen
import com.hjaquaculture.feature.sale.SaleScreen

/**
 * NavGraph (逻辑分发)
 * 在 Scaffold 的 content lambda 中放置 NavHost。这里定义了所有的导航路径。
 */
@Composable
fun NavGraph(
        navController: NavHostController,
        modifier: Modifier
) {

    val navigator = remember(navController) { RouteLogic(navController) }

    NavHost(
        navController = navController,
        startDestination = Screen.Login(initialUsername = null),
        modifier = modifier.padding(horizontal = 8.dp),
        /*
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
         */
        enterTransition = { NavAnimations.slideInFromRight() },
        exitTransition = { NavAnimations.slideOutToLeft() },
        popEnterTransition = { NavAnimations.slideInFromLeft() },
        popExitTransition = { NavAnimations.slideOutToRight()}
    ) {

        //登录页面
        composable<Screen.Login> { backStackEntry ->
            val viewModel: LoginViewModel = hiltViewModel()
            val loginRoute = backStackEntry.toRoute<Screen.Login>()

            // 确保在 ViewModel 创建或参数变化时，更新其内部状态
            LaunchedEffect(loginRoute.initialUsername) {
                loginRoute.initialUsername?.let {
                    viewModel.onUsernameChanged(it)
                }
            }
            LoginScreen(onAction = { action -> navigator.handleLoginAction(action) })
        }

        // 测试页面
        composable<Screen.Register>(
            enterTransition = { NavAnimations.slideInFromBottom() },
            exitTransition = { NavAnimations.slideOutToBottom() },
            popEnterTransition = { NavAnimations.slideInFromBottom() },
            popExitTransition = { NavAnimations.slideOutToBottom() },
        ){backStackEntry ->
            RegisterScreen(
                onAction = { action -> navigator.handleRegisterAction(action) }
            )
        }

        // 主页
        composable<Screen.Home> { backStackEntry ->
            HomeScreen(
                onAction = { action -> navigator.handleHomeAction(action)}
            )
        }

        composable < Screen.Sale>{
            SaleScreen()
        }

        composable<Screen.Purchase> {
            PurchaseScreen()
        }

        composable<Screen.SaleInvoice> {
            SaleInvoiceScreen()
        }
        composable<Screen.PurchaseInvoice>{
            PurchaseInvoiceScreen()
        }

        composable<Screen.PriceManage> {
            PriceManageScreen()
        }

        composable<Screen.Product> {
            ProductScreen()
        }
        composable<Screen.Relationship>{
            RelationshipScreen()
        }

        //-------- 以下是测试页面


        composable<Screen.Test.Home> {
            TestScreen(onAction = {action -> navigator.handleTestAction(action)})
        }
        composable<Screen.Test.AdaptiveApp> { backStackEntry ->
            AdaptiveScreen()
        }
        composable<Screen.Test.ThreePaneAdaptiveApp> { backStackEntry ->
            ThreePaneAdaptiveApp()
        }
        composable<Screen.Test.Sale> { backStackEntry ->
            SalesScreen()
        }
        composable <Screen.Test.Filter01>{
            OrderFilterScreen()
        }
        composable<Screen.Test.SwipeToReveal01> {
            SwipeToReveal_01()
        }
        composable<Screen.Test.SwipeToReveal02> {
            SwipeToReveal_02()
        }
        composable<Screen.Test.Sale3> {
            SaleScreen3()
        }
        composable<Screen.Test.MyAdaptiveApp2> {
            //AdaptiveScreen()
        }
    }
}
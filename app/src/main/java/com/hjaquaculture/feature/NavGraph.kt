package com.hjaquaculture.feature

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hjaquaculture.common.utils.NavAnimations
import com.hjaquaculture.feature.account.LoginScreen
import com.hjaquaculture.feature.account.LoginViewModel
import com.hjaquaculture.feature.account.RegisterScreen
import com.hjaquaculture.feature.home.HomeScreen
import com.hjaquaculture.feature.invoice.InvoiceScreen
import com.hjaquaculture.feature.order.OrderManagementScreen
import com.hjaquaculture.feature.order.OrderScreen
import com.hjaquaculture.feature.product.ProductScreen
import com.hjaquaculture.feature.relationship.RelationshipScreen
import com.hjaquaculture.feature.setting.SettingScreen

/**
 * NavGraph (逻辑分发)
 * 在 Scaffold 的 content lambda 中放置 NavHost。这里定义了所有的导航路径。
 */
@Composable
fun NavGraph(
        navController: NavHostController,
        scaffoldPadding: PaddingValues
) {

    val navigator = remember(navController) { RouteLogic(navController) }

    NavHost(
        navController = navController,
        startDestination = ScreenPage.Login(initialUsername = null),
        modifier = Modifier.fillMaxWidth(),
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

        composable<ScreenPage.Login> { backStackEntry ->
            // 这里其实是有问题的，在这里注入viewModel有可能会导致跳转卡顿，将来需要优化。
            val viewModel: LoginViewModel = hiltViewModel()
            val loginRoute = backStackEntry.toRoute<ScreenPage.Login>()

            // 确保在 ViewModel 创建或参数变化时，更新其内部状态
            LaunchedEffect(loginRoute.initialUsername) {
                loginRoute.initialUsername?.let {
                    viewModel.onUsernameChanged(it)
                }
            }
            LoginScreen(onAction = { action -> navigator.handleAuthAction(action) })
        }

        composable<ScreenPage.Register>(
            enterTransition = { NavAnimations.slideInFromBottom() },
            exitTransition = { NavAnimations.slideOutToBottom() },
            popEnterTransition = { NavAnimations.slideInFromBottom() },
            popExitTransition = { NavAnimations.slideOutToBottom() },
        ){backStackEntry ->
            RegisterScreen(
                onAction = { action -> navigator.handleAuthAction(action) }
            )
        }

        composable<ScreenPage.Home> { backStackEntry ->
            HomeScreen(
                onAction = { action -> navigator.handleAuthAction(action)},
                scaffoldPadding = scaffoldPadding
            )
        }
        composable<ScreenPage.Order> {
            OrderScreen(scaffoldPadding = scaffoldPadding)
        }

        composable<ScreenPage.OrderManagementScreen> {
            OrderManagementScreen(
                onAction = { action -> navigator.handleAuthAction(action)},
                scaffoldPadding = scaffoldPadding
            )
        }

        composable<ScreenPage.Product> {
            ProductScreen(
                onAction = { action -> navigator.handleAuthAction(action)},
                scaffoldPadding = scaffoldPadding
            )
        }

        composable<ScreenPage.Invoice> {
            InvoiceScreen(
                onAction = { action -> navigator.handleAuthAction(action)},
                scaffoldPadding = scaffoldPadding
            )
        }

        composable<ScreenPage.Relationship> {
            RelationshipScreen(
                onAction = { action -> navigator.handleAuthAction(action)},
                scaffoldPadding = scaffoldPadding
            )
        }

        composable<ScreenPage.Setting> {
            SettingScreen(
                onAction = { action -> navigator.handleAuthAction(action)},
                scaffoldPadding = scaffoldPadding
            )
        }

    }
}
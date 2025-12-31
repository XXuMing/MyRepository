package com.hjaquaculture.feature

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
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
import com.hjaquaculture.feature._temp.AdaptiveScreen
import com.hjaquaculture.feature._temp.SalesScreen
import com.hjaquaculture.feature._temp.ThreePaneAdaptiveApp
import com.hjaquaculture.feature.account.LoginScreen
import com.hjaquaculture.feature.account.LoginViewModel
import com.hjaquaculture.feature.account.RegisterScreen
import com.hjaquaculture.feature.home.HomeScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navigator = remember(navController) { ScreenRouteLogic(navController) }

    NavHost(
        navController = navController,
        startDestination = TempSaleScreenRoute,//LoginRoute(initialUsername = null) ,
        // 切换动画
        enterTransition = { NavAnimations.fadeEnter() },
        exitTransition = { NavAnimations.fadeExit() },
        popEnterTransition = { NavAnimations.fadeEnter() },
        popExitTransition = { NavAnimations.fadeExit() }

    ) {
        composable<LoginRoute>{ backStackEntry ->
            val viewModel: LoginViewModel = hiltViewModel()
            val loginRoute = backStackEntry.toRoute<LoginRoute>()

            // 确保在 ViewModel 创建或参数变化时，更新其内部状态
            LaunchedEffect(loginRoute.initialUsername) {
                loginRoute.initialUsername?.let {
                    viewModel.onUsernameChanged(it)
                }
            }
            LoginScreen(
                onAction = { action -> navigator.handleLoginAction(action) }
            )
        }

        composable<RegisterRoute>(
            enterTransition = { NavAnimations.slideInFromBottom() },
            exitTransition = { NavAnimations.slideOutToBottom() },
            popEnterTransition = { NavAnimations.slideInFromBottom() },
            popExitTransition = { NavAnimations.slideOutToBottom() },
        ){backStackEntry ->
            RegisterScreen(
                onAction = { action -> navigator.handleRegisterAction(action) }
            )
        }

        composable<HomeRoute> {backStackEntry ->
            HomeScreen()
        }

        composable<AdaptiveAppRoute> {backStackEntry ->
            AdaptiveScreen()
        }

        composable<ThreePaneAdaptiveAppRoute> {backStackEntry ->
            ThreePaneAdaptiveApp()
        }

        composable<TempSaleScreenRoute> { backStackEntry ->
            SalesScreen()
        }
    }
}
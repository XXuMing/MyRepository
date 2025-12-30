package com.hjaquaculture.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hjaquaculture.common.utils.NavAnimations
import com.hjaquaculture.feature._temp.AdaptiveScreen
import com.hjaquaculture.feature._temp.ThreePaneAdaptiveApp
import com.hjaquaculture.feature.account.LoginScreen
import com.hjaquaculture.feature.account.LoginViewModel
import com.hjaquaculture.feature.account.RegisterScreen
import com.hjaquaculture.feature.home.HomeScreen
import com.hjaquaculture.ui.theme.HJAquacultureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HJAquacultureTheme() {
                AppNavigation()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val navigator = remember(navController) { ScreenRouteLogic(navController) }

    NavHost(
        navController = navController,
        startDestination = LoginRoute(initialUsername = null) ,
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
    }
}

/*
enterTransition 的 lambda 其实提供了一个参数 AnimatedContentTransitionScope，你可以利用它判断 initialState（来源）和 targetState（去向）。
例子：
composable<Register>(
    enterTransition = {
    when (initialState.destination.route) {
        "com.hjaquaculture.feature.Login" -> {
            //它的值是类的完整路径字符串
            //如果来源是Login就运行另一种动画
        }
    }
    NavAnimations.slideInFromBottom() },
    exitTransition = { NavAnimations.slideOutToBottom() },
){
    RegisterActivity()
}
*/
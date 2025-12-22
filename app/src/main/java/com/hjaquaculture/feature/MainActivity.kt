package com.hjaquaculture.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hjaquaculture.common.utils.NavAnimations
import com.hjaquaculture.feature.account.LoginAction
import com.hjaquaculture.feature.account.LoginScreen
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


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(

        navController = navController,
        startDestination = Login,

        enterTransition = { NavAnimations.fadeEnter() },
        exitTransition = { NavAnimations.fadeExit() },
        popEnterTransition = { NavAnimations.fadeEnter() },
        popExitTransition = { NavAnimations.fadeExit() }

    ) {
        //路由部分
        composable<Login>{
            LoginScreen(onAction = {action ->
                when(action){
                    is LoginAction.GoToRegister -> navController.navigate(Register)
                    is LoginAction.LoginSuccess -> navController.navigate(Home)
                }
            })
        }

        composable<Register>{
            RegisterScreen()
        }

        composable<Home> {
            HomeScreen()
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
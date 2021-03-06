package com.compose.wechat.ui.main

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.compose.wechat.R
import com.compose.wechat.ui.main.chat.ChatScreen
import com.compose.wechat.ui.pay.PayContentScreen
import com.compose.wechat.ui.pay.PayPasswordInputScreen
import com.compose.wechat.ui.theme.isLaunchScreenShowed

object Router {
    const val LAUNCH = "launch"
    const val MAIN = "main"
    const val MESSAGE = "message"
    const val FRIENDS = "friends"
    const val MOMENTS = "moments"
    const val PROFILE = "profile"
    const val CHAT = "chat"
    const val PAY_VALIDATE = "payPasswordValidate"
    const val PAY_CONTENT = "payContent"
    const val PAY = "pay"
}

@Composable
fun MainNavGraph(statusBarColor: MutableState<Color>) {
    Log.d("MainNavGraph", "compose")
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Router.MAIN) {
        Log.d("NavHost", "compose")
        composable(Router.MAIN) {
            if (isLaunchScreenShowed) {
                Log.d("Main", "compose")
                val navList = mutableListOf(
                    NavItem(
                        stringResource(id = R.string.main_home),
                        Router.MESSAGE,
                        R.drawable.ic_main_home
                    ),
                    NavItem(
                        stringResource(id = R.string.main_friends),
                        Router.FRIENDS,
                        R.drawable.ic_main_friends
                    ),
                    NavItem(
                        stringResource(id = R.string.main_moments),
                        Router.MOMENTS,
                        R.drawable.ic_main_moments
                    ),
                    NavItem(
                        stringResource(id = R.string.main_profile),
                        Router.PROFILE,
                        R.drawable.ic_main_profile
                    )
                )
                NavWithBottomNavigation(
                    navController,
                    navList = navList,
                    statusBarColor = statusBarColor
                )
            } else {
                navController.navigate(Router.LAUNCH)
            }
        }
        composable(Router.LAUNCH) {
            Log.d("Launch", "compose")
            LaunchScreen(navController = navController, statusBarColor)
        }
        composable(
            "${Router.CHAT}/{id}/{name}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType }
            )
        ) {
            Log.d("Chat", "compose")
            val id = it.arguments?.getInt("id")
            val name = it.arguments?.getString("name")
            ChatScreen(navController = navController)
        }
        navigation(startDestination = Router.PAY_VALIDATE, route = Router.PAY) {
            composable(Router.PAY_VALIDATE) {
                PayPasswordInputScreen(navController, statusBarColor, {
                    Log.d(Router.PAY_VALIDATE, "setPwdComplete")
                    navController.apply {
                        popBackStack()
                        navigate(Router.PAY_CONTENT)
                    }
                }, {
                    Log.d(Router.PAY_VALIDATE, "inputPwdComplete")
                    navController.apply {
                        popBackStack()
                        navigate(Router.PAY_CONTENT)
                    }
                })
            }
            composable(Router.PAY_CONTENT) {
                PayContentScreen(navController, statusBarColor)
            }
        }
    }
}

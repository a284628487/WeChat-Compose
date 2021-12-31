package com.compose.wechat.main

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.compose.wechat.R
import com.compose.wechat.main.chat.ui.ChatScreen
import com.compose.wechat.ui.theme.isLaunchScreenShowed

object Router {
    const val LAUNCH = "launch"
    const val MAIN = "main"
    const val MESSAGE = "message"
    const val FRIENDS = "friends"
    const val MOMENTS = "moments"
    const val PROFILE = "profile"
    const val CHAT = "chat"
}

@Composable
fun MainNavGraph() {
    Log.d("MainNavGraph", "compose")
    val navController = rememberNavController()

    val indexState = remember {
        mutableStateOf(0)
    }

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
                NavWithBottomNavigation(navController, navList = navList)
            } else {
                navController.navigate(Router.LAUNCH)
            }
        }
        composable(Router.LAUNCH) {
            Log.d("Launch", "compose")
            LaunchScreen(navController = navController)
        }
        composable(
            "${Router.CHAT}/{id}/{name}",
            arguments = listOf(navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType })
        ) {
            Log.d("Chat", "compose")
            val id = it.arguments?.getInt("id")
            val name = it.arguments?.getString("name")
            ChatScreen(navController = navController)
        }
    }
}

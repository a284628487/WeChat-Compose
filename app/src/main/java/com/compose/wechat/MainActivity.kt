package com.compose.wechat

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.compose.wechat.ui.main.MainNavGraph
import com.compose.wechat.ui.common.LocalBackPressedDispatcher
import com.compose.wechat.ui.theme.WeChatTheme
import com.compose.wechat.ui.theme.isLaunchScreenShowed
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            WeChatTheme() {
                val systemUiController = rememberSystemUiController()
                val darkIcons = MaterialTheme.colors.isLight
                val initialColor = if (isLaunchScreenShowed) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.background
                }
                val titleBarBackgroundColor = remember {
                    mutableStateOf(initialColor)
                }

                // SideEffect {
                systemUiController.setStatusBarColor(
                    titleBarBackgroundColor.value,
                    darkIcons = darkIcons
                )
                // }
                systemUiController.setNavigationBarColor(MaterialTheme.colors.primary)

                ProvideWindowInsets {
                    CompositionLocalProvider(LocalBackPressedDispatcher provides this.onBackPressedDispatcher) {
                        // A surface container using the 'background' color from the theme
                        Surface(color = MaterialTheme.colors.background) {
                            MainNavGraph(titleBarBackgroundColor)
                        }
                    }
                }
            }
        }
    }
}
package com.compose.wechat

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import com.compose.wechat.main.MainNavGraph
import com.compose.wechat.ui.common.LocalBackPressedDispatcher
import com.compose.wechat.ui.theme.WeChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeChatTheme() {
                CompositionLocalProvider(LocalBackPressedDispatcher provides this.onBackPressedDispatcher) {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        MainNavGraph()
                    }
                }
            }
        }
//        val binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navController = findNavController(R.id.nav_main_fragment)
//        binding.navigationView.labelVisibilityMode = LABEL_VISIBILITY_LABELED
//        binding.navigationView.setupWithNavController(navController)
    }
}
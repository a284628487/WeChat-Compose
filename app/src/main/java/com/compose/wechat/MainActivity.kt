package com.compose.wechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.compose.wechat.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_LABELED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_main_fragment)
        binding.navigationView.labelVisibilityMode = LABEL_VISIBILITY_LABELED
        binding.navigationView.setupWithNavController(navController)
    }
}

//        setContent {
//            WeChatTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    Greeting("Android")
//                }
//            }
//        }
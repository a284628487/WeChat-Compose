package com.compose.wechat.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.compose.wechat.ui.theme.isLaunchScreenShowed
import kotlinx.coroutines.delay

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LaunchScreen(navController: NavHostController) {
    LaunchedEffect(key1 = "popBackStack", block = {
        delay(800)
        isLaunchScreenShowed = true
        navController.popBackStack()
    })
    Surface(color = MaterialTheme.colors.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Welcome~",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h4
            )
        }
    }
}

@Preview
@Composable
fun LaunchScreenPreview() {
    LaunchScreen(rememberNavController())
}
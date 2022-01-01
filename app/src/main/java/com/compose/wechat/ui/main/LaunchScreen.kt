package com.compose.wechat.ui.main

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.compose.wechat.ui.theme.isLaunchScreenShowed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LaunchScreen(navController: NavHostController) {
    Surface(color = MaterialTheme.colors.background) {
        val scope = rememberCoroutineScope()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AnimatedString() {
                scope.launch {
                    delay(400)
                    isLaunchScreenShowed = true
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun AnimatedString(callback: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        SingleAnimString("W", 0)
        SingleAnimString("e", 1)
        SingleAnimString("l", 2)
        SingleAnimString("c", 3)
        SingleAnimString("o", 4)
        SingleAnimString("m", 5)
        SingleAnimString("e", 6)
        SingleAnimString("~", 7) {
            callback()
        }
    }
}

@Composable
fun SingleAnimString(str: String, index: Int, callback: (() -> Unit)? = null) {
    var scaled by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = str, block = {
        delay(75L * index)
        scaled = true
    })
    val textSize by animateDpAsState(
        targetValue = if (scaled) 60.dp else 32.dp,
        animationSpec = tween(durationMillis = 100)
    ) {
        callback?.invoke()
    }
    if (textSize == 60.dp) {
        scaled = false
    }
    Text(
        text = str,
        modifier = Modifier.sizeIn(minWidth = 18.dp),
        color = MaterialTheme.colors.primary,
        style = TextStyle(
            Color.Red, fontSize = textSize.value.sp
        ),
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun LaunchScreenPreview() {
    LaunchScreen(rememberNavController())
}
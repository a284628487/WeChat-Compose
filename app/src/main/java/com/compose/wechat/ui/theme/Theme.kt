package com.compose.wechat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.compose.wechat.R

private val LightThemeColors = lightColors(
    primary = Red700,
    primaryVariant = Red900,
    onPrimary = Color.White,
    secondary = Red700,
    secondaryVariant = Red900,
    background = Background,
    onSecondary = Color.White,
    error = Red800,
    onBackground = Color.Black,
)

private val DarkThemeColors = darkColors(
    primary = Red300,
    primaryVariant = Red700,
    onPrimary = Color.Black,
    secondary = Red300,
    onSecondary = Color.Black,
    error = Red200,
    onBackground = Color.White
)

open class AbsThemeMode(var backgroundResId: Int, var logoResId: Int)

object LightThemeMode : AbsThemeMode(R.drawable.ic_launcher_background, R.drawable.ic_frag)

object DarkThemeMode : AbsThemeMode(R.drawable.ic_launcher_background, R.drawable.ic_frag)

internal var LocalThemeMode = staticCompositionLocalOf<AbsThemeMode> { LightThemeMode }

val MaterialTheme.themeMode
    @Composable
    @ReadOnlyComposable
    get() = LocalThemeMode.current

var isLaunchScreenShowed = false

@Composable
fun WeChatTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val welcomeAssets = if (darkTheme) DarkThemeMode else LightThemeMode
    CompositionLocalProvider(
        LocalThemeMode provides welcomeAssets,
    ) {
        val colors = if (darkTheme) {
            DarkThemeColors
        } else {
            LightThemeColors
        }

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
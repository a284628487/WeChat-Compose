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
    primary = Color(0xFFEDEDED),
    primaryVariant = Color(0xFFEDEDED),
    onPrimary = Color(0xFF171717),
    secondary = Color(0xFF06C05E),
    background = Color(0xFFFAFAFA),
    onSecondary = Color(0xFF171717),
    error = Color(0xffd00036),
    onBackground = Color(0xFF222222),
)

private val DarkThemeColors = darkColors(
    primary = Color(0xFF262626),
    primaryVariant = Color(0xFF262626),
    onPrimary = Color.White,
    secondary = Color(0xFF020202),
    background = Color(0xFF363636),
    onSecondary = Color.White,
    error = Color(0xffd00036),
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
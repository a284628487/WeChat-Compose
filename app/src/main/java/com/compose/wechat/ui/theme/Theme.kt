package com.compose.wechat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.compose.wechat.R

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

open class AbsThemeMode(var backgroundResId: Int, var logoResId: Int)

object LightThemeMode : AbsThemeMode(R.drawable.ic_launcher_background, R.drawable.ic_frag)

object DarkThemeMode : AbsThemeMode(R.drawable.ic_launcher_background, R.drawable.ic_frag)

internal var LocalThemeMode = staticCompositionLocalOf<AbsThemeMode> { LightThemeMode }

val MaterialTheme.themeMode
    @Composable
    @ReadOnlyComposable
    get() = LocalThemeMode.current

@Composable
fun WeChatTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val welcomeAssets = if (darkTheme) DarkThemeMode else LightThemeMode
    CompositionLocalProvider(
        LocalThemeMode provides welcomeAssets,
    ) {
        val colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}
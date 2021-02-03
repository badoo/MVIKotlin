package com.arkivanov.mvikotlin.timetravel.client.internal.compose.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.arkivanov.mvikotlin.timetravel.client.internal.compose.LocalUiConfig
import com.arkivanov.mvikotlin.timetravel.client.internal.compose.UiConfig

private val LightGreenColorPalette =
    lightColors(
        primary = indigo500,
        primaryVariant = indigo700,
        secondary = green200,
        secondaryVariant = green400,
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        error = Color.Red,
    )

private val DarkGreenColorPalette =
    darkColors(
        primary = indigo200,
        primaryVariant = indigo700,
        secondary = green200,
        secondaryVariant = green400,
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White,
        error = Color.Red,
    )

@Composable
fun TimeTravelClientTheme(
    isDarkMode: Boolean = false,
    uiConfig: UiConfig = UiConfig(),
    colorsOverride: (Colors) -> Colors = { it },
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalUiConfig provides uiConfig) {
        MaterialTheme(
            colors = colorsOverride(if (isDarkMode) DarkGreenColorPalette else LightGreenColorPalette),
            content = content
        )
    }
}

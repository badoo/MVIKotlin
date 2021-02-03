package com.arkivanov.mvikotlin.plugin.idea.timetravel

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.ui.UIUtil
import java.awt.Color as AWTColor

data class SwingColors(
    val isDarkMode: Boolean,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color
)

@Composable
fun rememberSwingColors(): State<SwingColors> {
    val swingColors = remember { mutableStateOf(getSwingColors()) }
    val messageBus = remember { ApplicationManager.getApplication().messageBus.connect() }

    DisposableEffect(messageBus) {
        messageBus.subscribe(
            LafManagerListener.TOPIC,
            LafManagerListener { swingColors.value = getSwingColors() }
        )

        onDispose(messageBus::disconnect)
    }

    return swingColors
}

fun SwingColors.getOverrideFunc(): (Colors) -> Colors =
    {
        it.copy(
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface
        )
    }

private fun getSwingColors(): SwingColors =
    SwingColors(
        isDarkMode = UIUtil.getPanelBackground().asComposeColor().isDark(),
        background = UIUtil.getListBackground().asComposeColor(),
        onBackground = UIUtil.getListForeground().asComposeColor(),
        surface = UIUtil.getPanelBackground().asComposeColor(),
        onSurface = UIUtil.getListForeground().asComposeColor()
    )

private fun AWTColor.asComposeColor(): Color = Color(red, green, blue, alpha)

private fun Color.isDark(): Boolean =
    0.299 * red + 0.587 * green + 0.114 * blue < 0.5

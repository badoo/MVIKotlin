package com.arkivanov.mvikotlin.timetravel.client.internal.compose

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalUiConfig: ProvidableCompositionLocal<UiConfig> = staticCompositionLocalOf(::UiConfig)

data class UiConfig(
    val toolbarButtonConfig: ToolbarButtonConfig = ToolbarButtonConfig()
)

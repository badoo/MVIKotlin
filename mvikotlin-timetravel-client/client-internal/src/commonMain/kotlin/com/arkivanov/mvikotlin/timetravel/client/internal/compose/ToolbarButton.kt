package com.arkivanov.mvikotlin.timetravel.client.internal.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
internal fun ToolbarButton(imageVector: ImageVector, enabled: Boolean = true, onClick: () -> Unit) {
    val config = LocalUiConfig.current.toolbarButtonConfig

    Box(
        modifier = Modifier
            .clickable(
                enabled = enabled,
                onClick = onClick,
                interactionSource = remember(::MutableInteractionSource),
                indication = rememberRipple(bounded = false)
            )
            .then(config.buttonModifier),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .alpha(if (enabled) LocalContentAlpha.current else ContentAlpha.disabled)
                .then(config.iconModifier),
            tint = contentColor(alpha = if (enabled) contentAlpha() else ContentAlpha.disabled)
        )
    }
}

data class ToolbarButtonConfig(
    val buttonModifier: Modifier = Modifier.size(48.dp),
    val iconModifier: Modifier = Modifier
)

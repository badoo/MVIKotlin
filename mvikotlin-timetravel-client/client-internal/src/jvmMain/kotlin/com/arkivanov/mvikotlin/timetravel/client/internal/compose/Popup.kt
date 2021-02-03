package com.arkivanov.mvikotlin.timetravel.client.internal.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
/*internal*/ actual fun Popup(
    alignment: Alignment,
    focusable: Boolean,
    onDismissRequest: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.window.Popup(
        alignment = alignment,
        focusable = focusable,
        onDismissRequest = onDismissRequest,
        content = content
    )
}

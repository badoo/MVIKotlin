package com.arkivanov.mvikotlin.timetravel.client.internal.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
/*internal*/ expect fun Popup(
    alignment: Alignment = Alignment.TopStart,
    focusable: Boolean = false,
    onDismissRequest: (() -> Unit)? = null,
    content: @Composable () -> Unit
)

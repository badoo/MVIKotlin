package com.arkivanov.mvikotlin.timetravel.client.internal.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
internal expect fun VerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
)

@Composable
internal expect fun VerticalScrollbar(
    lazyListState: LazyListState,
    itemCount: Int,
    averageItemSize: Dp,
    modifier: Modifier
)

@Composable
internal expect fun HorizontalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
)

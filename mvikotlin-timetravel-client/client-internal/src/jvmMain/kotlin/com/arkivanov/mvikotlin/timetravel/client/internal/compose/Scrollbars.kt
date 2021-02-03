package com.arkivanov.mvikotlin.timetravel.client.internal.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
internal actual fun VerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    androidx.compose.foundation.VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal actual fun VerticalScrollbar(
    lazyListState: LazyListState,
    itemCount: Int,
    averageItemSize: Dp,
    modifier: Modifier
) {
    androidx.compose.foundation.VerticalScrollbar(
        adapter = rememberScrollbarAdapter(
            scrollState = lazyListState,
            itemCount = itemCount,
            averageItemSize = averageItemSize
        ),
        modifier = modifier
    )
}

@Composable
internal actual fun HorizontalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    androidx.compose.foundation.HorizontalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier
    )
}

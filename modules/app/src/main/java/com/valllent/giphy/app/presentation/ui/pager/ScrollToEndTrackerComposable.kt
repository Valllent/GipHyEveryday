package com.valllent.giphy.app.presentation.ui.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun ScrollToEndTracker(
    lazyListState: LazyListState,
    loadWhenItemsToEnd: Int = 3,
    event: () -> Unit,
) {
    val isScrollToEnd by remember {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == lazyListState.layoutInfo.totalItemsCount - loadWhenItemsToEnd
        }
    }
    if (isScrollToEnd) {
        event()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollToEndTracker(
    pagerState: PagerState,
    itemsCount: Int,
    loadWhenItemsToEnd: Int = 3,
    event: () -> Unit,
) {
    val isScrollToEnd by remember {
        derivedStateOf {
            pagerState.currentPage == itemsCount - loadWhenItemsToEnd
        }
    }
    if (isScrollToEnd) {
        event()
    }
}
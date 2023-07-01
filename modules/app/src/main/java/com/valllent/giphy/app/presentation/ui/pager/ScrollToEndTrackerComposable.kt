package com.valllent.giphy.app.presentation.ui.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.*

@Composable
fun ScrollToEndTracker(
    lazyListState: LazyListState,
    loadWhenItemsToEnd: Int = 3,
    event: () -> Unit,
) {
    // Fix because Compose ignores new LazyListState.
    val state = remember { mutableStateOf(lazyListState) }
    state.value = lazyListState

    val isScrollToEnd by remember {
        derivedStateOf {
            val lastVisibleItemIndex = state.value.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            lastVisibleItemIndex == state.value.layoutInfo.totalItemsCount - loadWhenItemsToEnd
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
    pagerList: PagerList<*>,
    loadWhenItemsToEnd: Int = 3,
    event: () -> Unit,
) {
    val isScrollToEnd by remember {
        derivedStateOf {
            val itemsCount = pagerList.data.size
            pagerState.currentPage == itemsCount - loadWhenItemsToEnd
        }
    }
    LaunchedEffect(isScrollToEnd) {
        if (isScrollToEnd) {
            event()
        }
    }
}
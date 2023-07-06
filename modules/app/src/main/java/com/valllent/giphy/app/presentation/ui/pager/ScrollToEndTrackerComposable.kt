package com.valllent.giphy.app.presentation.ui.pager

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.*


private const val TAG = "ScrollToEndTracker"
private const val ACTIVATE_LOGS = false

private fun log(message: String) {
    if (ACTIVATE_LOGS) {
        Log.d(TAG, message)
    }
}

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
            val totalItemsCount = state.value.layoutInfo.totalItemsCount
            log("$lastVisibleItemIndex $totalItemsCount")
            lastVisibleItemIndex == totalItemsCount - loadWhenItemsToEnd
        }
    }
    if (isScrollToEnd) {
        log("Scroll to end")
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
    val pagerListState = remember { mutableStateOf(pagerList) }
    pagerListState.value = pagerList

    val isScrollToEnd by remember {
        derivedStateOf {
            val itemsCount = pagerListState.value.data.size
            val currentPage = pagerState.currentPage
            log("$itemsCount $currentPage")
            currentPage == itemsCount - loadWhenItemsToEnd
        }
    }
    LaunchedEffect(isScrollToEnd) {
        if (isScrollToEnd) {
            log("Scroll to end")
            event()
        }
    }
}
package com.valllent.giphy.app.presentation.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.valllent.giphy.app.presentation.ui.pager.LoadingState
import com.valllent.giphy.app.presentation.ui.pager.PagerList
import com.valllent.giphy.app.presentation.ui.pager.ScrollToEndTracker
import com.valllent.giphy.app.presentation.ui.utils.Retry
import kotlinx.coroutines.flow.Flow

@Composable
fun <T : Any> LazyListWithEventTracking(
    pagerList: PagerList<T>,
    firstLoading: @Composable () -> Unit,
    firstLoadingFailed: @Composable () -> Unit,
    loadingNewItems: @Composable () -> Unit,
    loadingNewItemsFailed: @Composable () -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    onScrollToEnd: () -> Unit,
    content: LazyListScope.() -> Unit,
) {
    ScrollToEndTracker(lazyListState) {
        onScrollToEnd()
    }

    when (pagerList.firstLoadingState) {
        LoadingState.NOT_LOADING -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
            ) {
                content()

                when (pagerList.appendLoadingState) {
                    LoadingState.LOADING -> {
                        item {
                            loadingNewItems()
                        }
                    }

                    LoadingState.FAILED -> {
                        item {
                            loadingNewItemsFailed()
                        }
                    }

                    else -> {}
                }
            }
        }

        LoadingState.LOADING -> {
            firstLoading()
        }

        LoadingState.FAILED -> {
            firstLoadingFailed()
        }
    }

}


@Composable
fun <T : Any> LazyListWithEventTracking(
    flow: Flow<PagingData<T>>,
    firstLoading: @Composable () -> Unit,
    firstLoadingFailed: @Composable (Retry) -> Unit,
    loadingNewItems: @Composable () -> Unit,
    loadingNewItemsFailed: @Composable (Retry) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    val lazyPagingItems = flow.collectAsLazyPagingItems()
    when (lazyPagingItems.loadState.refresh) {
        is LoadState.NotLoading -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
            ) {
                content()

                when (lazyPagingItems.loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            loadingNewItems()
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            loadingNewItemsFailed {
                                lazyPagingItems.retry()
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        is LoadState.Loading -> {
            firstLoading()
        }

        is LoadState.Error -> {
            firstLoadingFailed {
                lazyPagingItems.retry()
            }
        }
    }

}
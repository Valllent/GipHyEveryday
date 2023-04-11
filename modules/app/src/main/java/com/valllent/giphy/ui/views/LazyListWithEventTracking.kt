package com.valllent.giphy.ui.views

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
import com.valllent.giphy.ui.Retry
import kotlinx.coroutines.flow.Flow

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
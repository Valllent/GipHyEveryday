package com.valllent.giphy.app.presentation.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.valllent.giphy.app.presentation.ui.utils.Retry
import kotlinx.coroutines.flow.Flow
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> LazyPagerWithEventTracking(
    flow: Flow<PagingData<T>>,
    currentItemIndex: Int,
    getKey: (T) -> String,
    item: @Composable (T) -> Unit,
    loading: @Composable () -> Unit,
    loadingFailed: @Composable (Retry) -> Unit,
) {
    val lazyPagingItems = flow.collectAsLazyPagingItems()

    val appendState = lazyPagingItems.loadState.append

    val addAdditionalItem = appendState !is LoadState.NotLoading
    val itemsCount = if (lazyPagingItems.itemCount > 0) {
        lazyPagingItems.itemCount + if (addAdditionalItem) 1 else 0
    } else {
        0
    }

    HorizontalPager(
        itemsCount,
        state = rememberPagerState(currentItemIndex),
        key = {
            val isPlaceholder = addAdditionalItem && it == lazyPagingItems.itemCount
            if (isPlaceholder) {
                "Placeholder"
            } else {
                val item = lazyPagingItems.peek(it)
                if (item != null) {
                    getKey(item)
                } else {
                    UUID.randomUUID().toString()
                }
            }
        }
    ) { i ->
        val isPlaceholder = addAdditionalItem && i == lazyPagingItems.itemCount
        if (isPlaceholder) {
            if (appendState is LoadState.Loading) {
                loading()
            } else {
                loadingFailed {
                    lazyPagingItems.retry()
                }
            }
        } else {
            lazyPagingItems[i]?.let {
                item(it)
            }
        }
    }
}
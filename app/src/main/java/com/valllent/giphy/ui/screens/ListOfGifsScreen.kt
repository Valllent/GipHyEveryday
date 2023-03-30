@file:OptIn(ExperimentalMaterial3Api::class)

package com.valllent.giphy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.valllent.giphy.GifsViewModel
import com.valllent.giphy.R
import com.valllent.giphy.data.Gif
import com.valllent.giphy.ui.preview.GifPreviewData
import com.valllent.giphy.ui.theme.ProjectTheme
import com.valllent.giphy.ui.views.*
import com.valllent.giphy.ui.wrappers.ScaffoldWrapper
import kotlinx.coroutines.launch


typealias OnGifClick = (Int, Gif) -> Unit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListOfGifsScreen(
    viewModel: GifsViewModel,
    onItemClick: OnGifClick
) {
    val coroutineScope = rememberCoroutineScope()

    val lazyListState = rememberLazyListState()
    val searchFieldFocusRequester = FocusRequester()

    val gifsFlow = viewModel.gifsFlow
    val lazyPagingGifs = gifsFlow.collectAsLazyPagingItems()

    val refreshState = lazyPagingGifs.loadState.refresh
    val appendState = lazyPagingGifs.loadState.append
    val searchRequest = remember { mutableStateOf("") }
    val searchIsActivated = remember { mutableStateOf(false) }

    if (searchIsActivated.value) {
        LaunchedEffect(searchIsActivated.value) {
            if (lazyListState.firstVisibleItemIndex < 2) {
                lazyListState.animateScrollToItem(0)
            }
            searchFieldFocusRequester.requestFocus()
        }
    }

    ScaffoldWrapper(
        topAppBarActions = {
            val icon = if (searchIsActivated.value) Icons.Default.Close else Icons.Default.Search
            val description =
                stringResource(if (searchIsActivated.value) R.string.close else R.string.search)
            ProjectIconButton(
                imageVector = icon,
                contentDescription = description,
                onClick = {
                    searchIsActivated.value = searchIsActivated.value.not()
                }
            )
        },
        onTopAppBarLogoClick = {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(0)
            }
        }
    ) {

        when (refreshState) {
            is LoadState.NotLoading -> {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LazyColumn(
                        state = lazyListState
                    ) {
                        if (searchIsActivated.value) {
                            stickyHeader {
                                SearchField(
                                    searchRequest,
                                    searchFieldFocusRequester
                                )
                            }
                        }
                        itemsIndexed(
                            lazyPagingGifs,
                            key = { _, gif -> gif.id }
                        ) { i, gif ->
                            if (gif != null) {
                                GifItem(i, gif, onItemClick)
                            }
                        }

                        when (appendState) {
                            is LoadState.Loading -> {
                                item {
                                    InProgress(
                                        modifier = Modifier
                                            .height(100.dp),
                                        fraction = 0.6f
                                    )
                                }
                            }
                            is LoadState.Error -> {
                                item {
                                    DataFetchingFailed(
                                        modifier = Modifier.height(100.dp),
                                        onRetryClick = {
                                            lazyPagingGifs.retry()
                                        }
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }

            is LoadState.Loading -> {
                InProgress(
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is LoadState.Error -> {
                DataFetchingFailed(onRetryClick = {
                    lazyPagingGifs.retry()
                })
            }

        }
    }
}


@Composable
private fun GifItem(
    index: Int,
    gif: Gif,
    onItemClick: OnGifClick
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        TitleOnSurface(
            gif.title,
            modifier = Modifier.padding(8.dp),
            onClick = {
                onItemClick(index, gif)
            }
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(gif.width / gif.height.toFloat())
                .clickable {
                    onItemClick(index, gif)
                },
            tonalElevation = 24.dp,
            shadowElevation = 8.dp
        ) {
            ImageFromNetwork(gif.mediumUrl, gif.title, thumbnailUrl = gif.thumbnailUrl)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListOfGifsPreview() {
    ProjectTheme {
        Column {
            GifPreviewData.getList().forEachIndexed { i, gif ->
                GifItem(i, gif) { _, _ -> }
            }
        }
    }
}


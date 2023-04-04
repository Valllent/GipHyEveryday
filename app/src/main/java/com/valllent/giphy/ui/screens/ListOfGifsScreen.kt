@file:OptIn(ExperimentalMaterial3Api::class)

package com.valllent.giphy.ui.screens

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListOfGifsScreen(
    viewModel: GifsViewModel,
    onItemClick: OnGifClick
) {
    val searchIsActivated = rememberSaveable { mutableStateOf(false) }
    val focusRequestedAlready = rememberSaveable { mutableStateOf(false) }
    val currentGifsFlow = viewModel.currentGifsFlow.collectAsState()
    val lazyPagingGifs = currentGifsFlow.value.collectAsLazyPagingItems()
    val showingSearchResult = viewModel.showingSearchResult.collectAsState()

    val mainLazyListState = rememberLazyListState()
    var searchLazyListState = rememberLazyListState()
    val currentLazyListState = if (showingSearchResult.value) searchLazyListState else mainLazyListState
    val coroutineScope = rememberCoroutineScope()
    val searchFieldFocusRequester = remember { FocusRequester() }

    LaunchedEffect(searchIsActivated.value) {
        if (searchIsActivated.value) {
            if (focusRequestedAlready.value.not()) {
                if (currentLazyListState.firstVisibleItemIndex in 0..5) {
                    currentLazyListState.animateScrollToItem(0)
                }
                searchFieldFocusRequester.requestFocus()
            }
            focusRequestedAlready.value = true
        } else {
            focusRequestedAlready.value = false
            viewModel.closeSearch()
        }
    }

    ScaffoldWrapper(
        topAppBarActions = {
            val icon = if (searchIsActivated.value) Icons.Default.Close else Icons.Outlined.Search
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
                currentLazyListState.animateScrollToItem(0)
            }
        }
    ) {

        when (lazyPagingGifs.loadState.refresh) {
            is LoadState.NotLoading -> {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LazyColumn(
                        state = currentLazyListState,
                    ) {
                        if (searchIsActivated.value) {
                            stickyHeader(
                                key = "Search"
                            ) {
                                val backPressedDispatcher =
                                    (LocalContext.current as ComponentActivity).onBackPressedDispatcher
                                val backListener = remember {
                                    object : OnBackPressedCallback(true) {
                                        override fun handleOnBackPressed() {
                                            searchIsActivated.value = false
                                            viewModel.closeSearch()
                                        }
                                    }
                                }

                                DisposableEffect(backPressedDispatcher) {
                                    backPressedDispatcher.addCallback(backListener)
                                    onDispose {
                                        backListener.remove()
                                    }
                                }

                                SearchField(
                                    request = viewModel.searchRequest.collectAsState().value,
                                    enabled = viewModel.searchRequestIsCorrect.value,
                                    onSearchClick = {
                                        viewModel.search()
                                    },
                                    onSearchRequestChange = {
                                        viewModel.setSearchRequest(it)
                                    },
                                    focusRequester = searchFieldFocusRequester
                                )
                            }
                        }
                        itemsIndexed(
                            lazyPagingGifs,
                            key = { i, gif ->
                                gif.generatedUniqueId
                            }
                        ) { i, gif ->
                            if (gif != null) {
                                GifItem(i, gif, onItemClick)
                            }
                        }

                        when (lazyPagingGifs.loadState.append) {
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


@file:OptIn(ExperimentalMaterial3Api::class)

package com.valllent.giphy.app.presentation.ui.screens.trending

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.valllent.giphy.R
import com.valllent.giphy.app.presentation.data.preview.GifPreviewData
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.GlobalListeners
import com.valllent.giphy.app.presentation.ui.theme.ProjectTheme
import com.valllent.giphy.app.presentation.ui.utils.OnGifClick
import com.valllent.giphy.app.presentation.ui.views.*
import com.valllent.giphy.app.presentation.ui.wrappers.ScaffoldWrapper
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    state: TrendingScreenState,
    actions: TrendingScreenActions,
    globalListeners: GlobalListeners,
) {
    val mainLazyListState = rememberLazyListState()
    val searchLazyListState = rememberLazyListState()
    val searchFieldFocusRequester = remember { FocusRequester() }

    val currentLazyListState = if (state.showSearchResultList) searchLazyListState else mainLazyListState

    val coroutineScope = rememberCoroutineScope()
    val lazyPagingGifs = state.currentGifsFlow.collectAsLazyPagingItems()

    LaunchedEffect(state.showSearchField) {
        if (state.showSearchField) {
            if (state.searchFieldFocusRequestedAlready.not()) {
                if (currentLazyListState.firstVisibleItemIndex in 0..5) {
                    currentLazyListState.animateScrollToItem(0)
                }
                searchFieldFocusRequester.requestFocus()
            }
            actions.onSearchFieldFocusRequested()
        }
    }

    ScaffoldWrapper(
        topAppBarActions = {
            val icon = if (state.showSearchField) Icons.Default.Close else Icons.Outlined.Search
            val description = stringResource(if (state.showSearchField) R.string.close else R.string.search)
            ProjectIconButton(
                imageVector = icon,
                contentDescription = description,
                onClick = {
                    if (state.showSearchField) {
                        actions.onCloseSearch()
                    } else {
                        actions.onOpenSearch()
                    }
                }
            )
        },
        onTopAppBarLogoClick = {
            coroutineScope.launch {
                currentLazyListState.animateScrollToItem(0)
            }
        },
        globalListeners = globalListeners,
    ) {

        LazyListWithEventTracking(
            flow = state.currentGifsFlow,
            lazyListState = currentLazyListState,
            firstLoading = {
                InProgress(
                    modifier = Modifier.fillMaxSize(),
                )
            },
            firstLoadingFailed = { retry ->
                DataFetchingFailed(onRetryClick = {
                    retry()
                })
            },
            loadingNewItems = {
                InProgress(
                    modifier = Modifier
                        .height(100.dp),
                    fraction = 0.6f
                )
            },
            loadingNewItemsFailed = { retry ->
                DataFetchingFailed(
                    modifier = Modifier.height(100.dp),
                    onRetryClick = {
                        retry()
                    }
                )
            }
        ) {

            if (state.showSearchField) {
                stickyHeader(
                    key = "Search"
                ) {
                    BackStackEntryComposable(
                        onBackPressedListener = {
                            actions.onCloseSearch()
                        }
                    )
                    SearchField(
                        request = state.searchRequest,
                        enabled = state.searchRequestIsCorrect,
                        onSearchClick = {
                            coroutineScope.launch {
                                searchLazyListState.scrollToItem(0)
                            }
                            actions.onSearchClick()
                        },
                        onSearchRequestChange = {
                            actions.onSearchRequestChange(it)
                        },
                        focusRequester = searchFieldFocusRequester
                    )
                }
            }
            items(
                lazyPagingGifs.itemCount,
                key = lazyPagingGifs.itemKey {
                    it.uniqueId
                },
            ) { i ->
                val gif = lazyPagingGifs[i]
                if (gif != null) {
                    GifItem(
                        i,
                        gif,
                        onSaveClick = {
                            actions.onChangeSavedStateForGif(gif)
                        },
                        onItemClick = { index, clickedGif ->
                            actions.onGifClick(index, clickedGif)
                        }
                    )
                }
            }

        }
    }
}


@Composable
private fun GifItem(
    index: Int,
    gif: GifUiModel,
    onSaveClick: () -> Unit,
    onItemClick: OnGifClick,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val (titleRef, iconRef) = createRefs()

            TitleOnSurface(
                gif.title,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(titleRef) {
                        this.top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(iconRef.start)
                        width = Dimension.preferredWrapContent
                        height = Dimension.preferredWrapContent
                    },
                onClick = {
                    onItemClick(index, gif)
                }
            )

            ProjectIconButton(
                if (gif.isSaved.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                stringResource(R.string.save_gif),
                onClick = {
                    onSaveClick()
                },
                modifier = Modifier
                    .constrainAs(iconRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            )
        }
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
fun ListOfGifsPreview_1() {
    ProjectTheme {
        Column {
            GifItem(0, GifPreviewData.getList()[0], { true }) { _, _ -> }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfGifsPreview_2() {
    ProjectTheme {
        Column {
            GifItem(1, GifPreviewData.getList()[1], { true }) { _, _ -> }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfGifsPreview_3() {
    ProjectTheme {
        Column {
            GifItem(2, GifPreviewData.getList()[2], { true }) { _, _ -> }
        }
    }
}


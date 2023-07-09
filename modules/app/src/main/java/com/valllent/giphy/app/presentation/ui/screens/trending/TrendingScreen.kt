@file:OptIn(ExperimentalMaterial3Api::class)

package com.valllent.giphy.app.presentation.ui.screens.trending

import android.util.Log
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import com.valllent.giphy.R
import com.valllent.giphy.app.presentation.data.preview.GifPreviewData
import com.valllent.giphy.app.presentation.ui.GlobalListeners
import com.valllent.giphy.app.presentation.ui.screens.detail.OpenDetailScreenLambda
import com.valllent.giphy.app.presentation.ui.theme.ProjectTheme
import com.valllent.giphy.app.presentation.ui.utils.OnLifecycleEvent
import com.valllent.giphy.app.presentation.ui.views.*
import com.valllent.giphy.app.presentation.ui.wrappers.ScaffoldWrapper
import com.valllent.giphy.domain.data.Gif
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    state: TrendingScreenState,
    actions: TrendingScreenActions,
    globalListeners: GlobalListeners,
) {
    val coroutineScope = rememberCoroutineScope()
    val trendingLazyListState = rememberLazyListState()
    val searchLazyListState = rememberLazyListState()
    val focusRequestedAlready = remember { mutableStateOf(false) }
    val searchFieldFocusRequester = remember { FocusRequester() }
    val currentLazyListState = remember { mutableStateOf(trendingLazyListState) }
    val currentPagerType = remember { mutableStateOf(OpenDetailScreenLambda.PagerType.TRENDING) }

    val pagerList = state.gifs.collectAsState().value

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                actions.onReturnToPage()
            }

            else -> {}
        }
    }

    LaunchedEffect(state.showSearchResultList) {
        currentLazyListState.value = if (state.showSearchResultList) searchLazyListState else trendingLazyListState
        currentPagerType.value =
            if (state.showSearchResultList) OpenDetailScreenLambda.PagerType.SEARCH else OpenDetailScreenLambda.PagerType.TRENDING
    }

    LaunchedEffect(state.showSearchField) {
        delay(300)
        if (state.showSearchField && !focusRequestedAlready.value) {
            focusRequestedAlready.value = true
            if (currentLazyListState.value.firstVisibleItemIndex in 0..3) {
                currentLazyListState.value.animateScrollToItem(0)
            }
            try {
                searchFieldFocusRequester.requestFocus()
            } catch (any: Throwable) {
                Log.d("TrendingScreen", "Can't request focus")
            }
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
                        focusRequestedAlready.value = false
                    } else {
                        actions.onOpenSearch()
                    }
                }
            )
        },
        onTopAppBarLogoClick = {
            coroutineScope.launch {
                currentLazyListState.value.animateScrollToItem(0)
            }
        },
        globalListeners = globalListeners,
    ) {
        LazyListWithEventTracking(
            pagerList = pagerList,
            lazyListState = currentLazyListState.value,
            firstLoading = {
                InProgress(
                    modifier = Modifier.fillMaxSize(),
                )
            },
            firstLoadingFailed = {
                DataFetchingFailed(onRetryClick = {
                    actions.onLoadNextPageOrRetry()
                })
            },
            loadingNewItems = {
                InProgress(
                    modifier = Modifier
                        .height(100.dp),
                    fraction = 0.6f
                )
            },
            loadingNewItemsFailed = {
                DataFetchingFailed(
                    modifier = Modifier.height(100.dp),
                    onRetryClick = {
                        actions.onLoadNextPageOrRetry()
                    }
                )
            },
            onScrollToEnd = {
                actions.onLoadNextPageOrRetry()
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
                pagerList.data.size,
                key = { index ->
                    pagerList.data[index].uniqueId
                }
            ) { i ->
                val gif = pagerList.data[i]
                GifItem(
                    i,
                    gif,
                    onSaveClick = {
                        actions.onChangeSavedStateForGif(gif.id)
                    },
                    onItemClick = { index ->
                        actions.onGifClick.run(
                            if (state.showSearchResultList) {
                                OpenDetailScreenLambda.Arguments.Search(index, state.searchRequest)
                            } else {
                                OpenDetailScreenLambda.Arguments.Trending(index)
                            }
                        )
                    }
                )
            }

        }
    }
}


@Composable
private fun GifItem(
    index: Int,
    gif: Gif,
    onSaveClick: () -> Unit,
    onItemClick: (Int) -> Unit,
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
                    onItemClick(index)
                }
            )

            ProjectIconButton(
                if (gif.isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
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
                    onItemClick(index)
                },
            tonalElevation = 24.dp,
            shadowElevation = 8.dp
        ) {
            ImageFromNetwork(
                url = gif.mediumUrl,
                contentDescription = gif.title,
                thumbnailUrl = gif.thumbnailUrl
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListOfGifsPreview_1() {
    ProjectTheme {
        Column {
            GifItem(0, GifPreviewData.getList()[0], { true }) { _ -> }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfGifsPreview_2() {
    ProjectTheme {
        Column {
            GifItem(1, GifPreviewData.getList()[1], { true }) { _ -> }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfGifsPreview_3() {
    ProjectTheme {
        Column {
            GifItem(2, GifPreviewData.getList()[2], { true }) { _ -> }
        }
    }
}


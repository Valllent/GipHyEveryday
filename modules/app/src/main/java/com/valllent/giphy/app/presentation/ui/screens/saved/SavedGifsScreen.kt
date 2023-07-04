package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.valllent.giphy.R
import com.valllent.giphy.app.presentation.data.preview.GifPreviewData
import com.valllent.giphy.app.presentation.ui.GlobalListeners
import com.valllent.giphy.app.presentation.ui.pager.LoadingState
import com.valllent.giphy.app.presentation.ui.screens.detail.OpenDetailScreenLambda
import com.valllent.giphy.app.presentation.ui.theme.ProjectTheme
import com.valllent.giphy.app.presentation.ui.views.*
import com.valllent.giphy.app.presentation.ui.wrappers.ScaffoldWrapper
import com.valllent.giphy.domain.data.Gif
import kotlinx.coroutines.launch

@Composable
fun SavedGifsScreen(
    state: SavedGifsState,
    actions: SavedGifsActions,
    globalListeners: GlobalListeners
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val pagerList = state.pagerList.collectAsState().value
    val gifs = pagerList.data

    ScaffoldWrapper(
        onTopAppBarLogoClick = {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(0)
            }
        },
        globalListeners = globalListeners,
    ) {
        if (gifs.isEmpty() && pagerList.firstLoadingState == LoadingState.NOT_LOADING) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.empty_saved_list),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        } else {
            LazyListWithEventTracking(
                pagerList = pagerList,
                firstLoading = {
                    InProgress(
                        modifier = Modifier.fillMaxSize(),
                    )
                },
                firstLoadingFailed = { ->
                    DataFetchingFailed(onRetryClick = {
                        actions.onLoadNextPagerOrRetry()
                    })
                },
                loadingNewItems = {
                    InProgress(
                        modifier = Modifier
                            .height(100.dp),
                        fraction = 0.6f
                    )
                },
                loadingNewItemsFailed = { ->
                    DataFetchingFailed(
                        modifier = Modifier.height(100.dp),
                        onRetryClick = {
                            actions.onLoadNextPagerOrRetry()
                        }
                    )
                },
                onScrollToEnd = {
                    actions.onLoadNextPagerOrRetry()
                }
            ) {
                items(
                    gifs.size,
                    key = {
                        gifs[it].uniqueId
                    }
                ) { i ->
                    val gif = gifs[i]
                    GifItem(
                        i,
                        gif,
                        onSaveClick = actions.onChangeSavedStateForGif,
                        onItemClick = actions.onGifItemClick
                    )
                }

            }
        }
    }
}


@Composable
private fun GifItem(
    index: Int,
    gif: Gif,
    onSaveClick: (String) -> Unit,
    onItemClick: OpenDetailScreenLambda,
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
                    onItemClick.run(OpenDetailScreenLambda.Arguments.Saved(index))
                }
            )

            ProjectIconButton(
                if (gif.isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                stringResource(R.string.save_gif),
                onClick = {
                    onSaveClick(gif.id)
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
                    onItemClick.run(OpenDetailScreenLambda.Arguments.Saved(index))
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
fun ListOfSavedGifsPreview_1() {
    ProjectTheme {
        Column {
            GifItem(0, GifPreviewData.getList()[0], { true }) { _ -> }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfSavedGifsPreview_2() {
    ProjectTheme {
        Column {
            GifItem(1, GifPreviewData.getList()[1], { true }) { _ -> }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfSavedGifsPreview_3() {
    ProjectTheme {
        Column {
            GifItem(2, GifPreviewData.getList()[2], { true }) { _ -> }
        }
    }
}


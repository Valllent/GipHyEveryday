package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.valllent.giphy.app.presentation.ui.screens.detail.OpenDetailScreenLambda
import com.valllent.giphy.app.presentation.ui.theme.ProjectTheme
import com.valllent.giphy.app.presentation.ui.views.*
import com.valllent.giphy.app.presentation.ui.wrappers.ScaffoldWrapper
import kotlinx.coroutines.launch

@Composable
fun SavedGifsScreen(
    state: SavedGifsState,
    actions: SavedGifsActions,
    globalListeners: GlobalListeners
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val lazyPagingGifs = state.gifsFlow.collectAsLazyPagingItems()

    ScaffoldWrapper(
        onTopAppBarLogoClick = {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(0)
            }
        },
        globalListeners = globalListeners,
    ) {

        LazyListWithEventTracking(
            flow = state.gifsFlow,
            lazyListState = lazyListState,
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
            items(
                lazyPagingGifs.itemCount,
                key = lazyPagingGifs.itemKey {
                    it.uniqueId
                },
            ) { i ->
                val gif = lazyPagingGifs.peek(i)
                if (gif != null) {
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
    gif: GifUiModel,
    onSaveClick: (GifUiModel) -> Unit,
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
                if (gif.isSaved.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                stringResource(R.string.save_gif),
                onClick = {
                    onSaveClick(gif)
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


package com.valllent.giphy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.valllent.giphy.R
import com.valllent.giphy.domain.data.Gif
import com.valllent.giphy.ui.GlobalListeners
import com.valllent.giphy.ui.data.preview.GifPreviewData
import com.valllent.giphy.ui.theme.ProjectTheme
import com.valllent.giphy.ui.utils.OnGifClick
import com.valllent.giphy.ui.viewmodels.SavedGifsViewModel
import com.valllent.giphy.ui.views.*
import com.valllent.giphy.ui.wrappers.ScaffoldWrapper
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListOfSavedGifsScreen(
    viewModel: SavedGifsViewModel,
    onItemClick: OnGifClick,
    globalListeners: GlobalListeners
) {
    val lazyListState = rememberLazyListState()

    val currentGifsFlow = viewModel.currentGifsFlow.collectAsState()
    val lazyPagingGifs = currentGifsFlow.value.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()

    ScaffoldWrapper(
        onTopAppBarLogoClick = {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(0)
            }
        },
        globalListeners = globalListeners,
    ) {

        LazyListWithEventTracking(
            flow = currentGifsFlow.value,
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
            itemsIndexed(
                lazyPagingGifs,
                key = { i, gif ->
                    gif.generatedUniqueId
                }
            ) { i, gif ->
                if (gif != null) {
                    GifItem(
                        i,
                        gif,
                        onSaveClick = { viewModel.changeSavedState(gif) },
                        onItemClick = onItemClick
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
    onSaveClick: suspend () -> Boolean,
    onItemClick: OnGifClick,
) {
    val coroutineScope = rememberCoroutineScope()
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

            val isSavedState = remember { mutableStateOf(gif.isSaved) }
            ProjectIconButton(
                if (isSavedState.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                stringResource(R.string.save_gif),
                onClick = {
                    coroutineScope.launch {
                        val newValue = onSaveClick()
                        isSavedState.value = newValue
                    }
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
fun ListOfSavedGifsPreview_1() {
    ProjectTheme {
        Column {
            GifItem(0, GifPreviewData.getList()[0], { true }) { _, _ -> }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfSavedGifsPreview_2() {
    ProjectTheme {
        Column {
            GifItem(1, GifPreviewData.getList()[1], { true }) { _, _ -> }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfSavedGifsPreview_3() {
    ProjectTheme {
        Column {
            GifItem(2, GifPreviewData.getList()[2], { true }) { _, _ -> }
        }
    }
}


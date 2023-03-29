package com.valllent.giphy.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.valllent.giphy.GifsViewModel
import com.valllent.giphy.data.Gif
import com.valllent.giphy.ui.preview.GifPreviewData
import com.valllent.giphy.ui.theme.ProjectTheme
import com.valllent.giphy.ui.views.DataFetchingFailed
import com.valllent.giphy.ui.views.ImageFromNetwork
import com.valllent.giphy.ui.views.InProgress
import com.valllent.giphy.ui.views.TitleOnSurface


typealias OnGifClick = (Int, Gif) -> Unit

@Composable
fun ListOfGifsScreen(
    viewModel: GifsViewModel,
    onItemClick: OnGifClick
) {
    val gifsFlow = viewModel.gifsFlow
    val lazyPagingItems = gifsFlow.collectAsLazyPagingItems()
    ListOfGifs(lazyPagingItems, onItemClick)
}

@Composable
private fun ListOfGifs(lazyListOfGifs: LazyPagingItems<Gif>, onItemClick: OnGifClick) {
    val refreshState = lazyListOfGifs.loadState.refresh
    val appendState = lazyListOfGifs.loadState.append

    when (refreshState) {
        is LoadState.NotLoading -> {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn {
                    itemsIndexed(
                        lazyListOfGifs,
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
                                        lazyListOfGifs.retry()
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
                lazyListOfGifs.retry()
            })
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
                GifItem(i, gif) { i, gif -> }
            }
        }
    }
}


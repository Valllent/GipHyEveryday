package com.valllent.giphy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.valllent.giphy.GifsViewModel
import com.valllent.giphy.data.Gif
import com.valllent.giphy.ui.preview.GifPreviewData
import com.valllent.giphy.ui.views.*
import com.valllent.giphy.ui.wrappers.PreviewWrapper
import com.valllent.giphy.ui.wrappers.ScaffoldWrapper

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailGifScreen(gifsViewModel: GifsViewModel, selectedGifIndex: Int) {
    ScaffoldWrapper {

        val lazyGifs = gifsViewModel.gifsFlow.collectAsLazyPagingItems()
        val appendState = lazyGifs.loadState.append

        val addAdditionalItem = appendState !is LoadState.NotLoading
        val itemsCount = if (lazyGifs.itemCount > 0) {
            lazyGifs.itemCount + if (addAdditionalItem) 1 else 0
        } else {
            0
        }

        HorizontalPager(
            itemsCount,
            state = rememberPagerState(selectedGifIndex),
            key = { i ->
                val isPlaceholder = addAdditionalItem && i == lazyGifs.itemCount
                if (isPlaceholder) {
                    "Placeholder"
                } else {
                    lazyGifs.peek(i)?.id ?: ""
                }
            }
        ) { i ->
            val isPlaceholder = addAdditionalItem && i == lazyGifs.itemCount
            if (isPlaceholder) {
                if (appendState is LoadState.Loading) {
                    InProgress()
                } else {
                    DataFetchingFailed(
                        onRetryClick = {
                            lazyGifs.retry()
                        }
                    )
                }
            } else {
                lazyGifs[i]?.let {
                    DetailGif(it)
                }
            }
        }
    }
}

@Composable
private fun DetailGif(gif: Gif) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TitleOnSurface(
            gif.title,
            modifier = Modifier.padding(8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(gif.width.toFloat() / gif.height)
        ) {
            ImageFromNetwork(gif.mediumUrl, gif.title)
        }
        if (gif.postedBy.isNotBlank()) {
            SubtitleOnSurface(
                text = gif.postedBy,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .align(Alignment.End),
                onClick = {

                }
            )
        }
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Surface(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(
                text = gif.postedDatetime,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailGifScreenPreview() {
    PreviewWrapper {
        DetailGif(GifPreviewData.getList().first())
    }
}
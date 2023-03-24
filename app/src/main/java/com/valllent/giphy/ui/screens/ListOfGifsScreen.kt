package com.valllent.giphy.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    when (viewModel.dataFetchingStatus.value) {
        GifsViewModel.DataFetchingStatus.FETCHED -> {
            ListOfGifs(viewModel.gifs.value, onItemClick)
        }

        GifsViewModel.DataFetchingStatus.IN_PROGRESS -> {
            InProgress()
        }

        GifsViewModel.DataFetchingStatus.FAILED -> {
            DataFetchingFailed(onRetryClick = {
                viewModel.fetchGifs()
            })
        }
    }
}

@Composable
private fun ListOfGifs(listOfGifs: List<Gif>, onItemClick: OnGifClick) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn {
            itemsIndexed(
                listOfGifs,
                key = { _, gif -> gif.id }
            ) { i, gif ->
                GifItem(i, gif, onItemClick)
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
        Modifier.fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        TitleOnSurface(
            gif.title,
            modifier = Modifier.padding(8.dp),
            onClick = {
                onItemClick(index, gif)
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(gif.width / gif.height.toFloat())
                .clickable {
                    onItemClick(index, gif)
                }
        ) {
            ImageFromNetwork(gif.mediumUrl, gif.title, thumbnailUrl = gif.thumbnailUrl)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListOfGifsPreview() {
    ProjectTheme {
        ListOfGifs(GifPreviewData.getList()) { _, _ -> }
    }
}


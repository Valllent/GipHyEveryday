package com.valllent.giphy.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valllent.giphy.GifsViewModel
import com.valllent.giphy.R
import com.valllent.giphy.data.Gif
import com.valllent.giphy.ui.preview.GifPreviewData
import com.valllent.giphy.ui.views.ImageFromNetwork
import com.valllent.giphy.ui.views.SubtitleOnSurface
import com.valllent.giphy.ui.views.TitleOnSurface
import com.valllent.giphy.ui.wrappers.PreviewWrapper

@Composable
fun DetailGifScreen(gifsViewModel: GifsViewModel, selectedGifIndex: Int) {
    val gifs = gifsViewModel.gifs.value
    DetailGifPager(gifs, selectedGifIndex)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailGifPager(gifs: List<Gif>, selectedGifIndex: Int) {
    val pagerState = rememberPagerState(selectedGifIndex)
    HorizontalPager(
        gifs.size,
        state = pagerState
    ) { i ->
        DetailGif(gifs[i])
    }
}

@Composable
private fun DetailGif(gif: Gif) {
    Column(
        modifier = Modifier.fillMaxSize()
            .scrollable(rememberScrollableState { 0f }, Orientation.Vertical)
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
        Text(
            text = stringResource(R.string.posted) + gif.postedDatetime,
            modifier = Modifier.padding(8.dp),
            fontSize = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailGifScreenPreview() {
    PreviewWrapper {
        DetailGif(GifPreviewData.getList().first())
    }
}
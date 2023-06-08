package com.valllent.giphy.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
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
import androidx.paging.PagingData
import com.valllent.giphy.app.presentation.data.preview.GifPreviewData
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.GlobalListeners
import com.valllent.giphy.app.presentation.ui.views.*
import com.valllent.giphy.app.presentation.ui.wrappers.PreviewWrapper
import com.valllent.giphy.app.presentation.ui.wrappers.ScaffoldWrapper
import kotlinx.coroutines.flow.Flow

@Composable
fun DetailGifScreen(flow: Flow<PagingData<GifUiModel>>, selectedGifIndex: Int, globalListeners: GlobalListeners) {
    ScaffoldWrapper(
        globalListeners = globalListeners
    ) {
        LazyPagerWithEventTracking(
            flow = flow,
            currentItemIndex = selectedGifIndex,
            getKey = {
                it.uniqueId
            },
            item = {
                DetailGif(it)
            },
            loading = {
                InProgress()
            },
            loadingFailed = { retry ->
                DataFetchingFailed(
                    onRetryClick = {
                        retry()
                    }
                )
            }
        )
    }
}

@Composable
private fun DetailGif(gif: GifUiModel) {
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
package com.valllent.giphy.app.presentation.ui.screens.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.valllent.giphy.R
import com.valllent.giphy.app.presentation.data.DateTimeUtils
import com.valllent.giphy.app.presentation.data.preview.GifPreviewData
import com.valllent.giphy.app.presentation.ui.GlobalListeners
import com.valllent.giphy.app.presentation.ui.utils.OnLifecycleEvent
import com.valllent.giphy.app.presentation.ui.views.DataFetchingFailed
import com.valllent.giphy.app.presentation.ui.views.ImageFromNetwork
import com.valllent.giphy.app.presentation.ui.views.InProgress
import com.valllent.giphy.app.presentation.ui.views.LazyPagerWithEventTracking
import com.valllent.giphy.app.presentation.ui.views.ProjectIconButton
import com.valllent.giphy.app.presentation.ui.views.SubtitleOnSurface
import com.valllent.giphy.app.presentation.ui.views.TitleOnSurface
import com.valllent.giphy.app.presentation.ui.wrappers.PreviewWrapper
import com.valllent.giphy.app.presentation.ui.wrappers.ScaffoldWrapper
import com.valllent.giphy.domain.data.Gif

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailGifScreen(
    state: DetailGifScreenState,
    actions: DetailGifScreenActions,
    globalListeners: GlobalListeners
) {
    val pagerList = state.pagerListFlow.collectAsState().value
    val pagerState = rememberPagerState(state.currentItemIndex)

    val currentGif = pagerList.data.getOrNull(pagerState.currentPage)

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                actions.onReturnToPage()
            }

            else -> {}
        }
    }

    ScaffoldWrapper(
        topAppBarActions = {
            val isSaved = currentGif?.isSaved ?: false
            val icon = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
            val description = stringResource(if (isSaved) R.string.unsave_gif else R.string.save_gif)
            ProjectIconButton(
                imageVector = icon,
                contentDescription = description,
                onClick = {
                    actions.onChangeSavedStateForGif(checkNotNull(currentGif?.id))
                }
            )
        },
        globalListeners = globalListeners
    ) {

        LazyPagerWithEventTracking(
            pagerList,
            pagerState = pagerState,
            getKey = {
                it.uniqueId
            },
            item = {
                DetailGif(it)
            },
            loading = {
                InProgress()
            },
            loadingFailed = {
                DataFetchingFailed(
                    onRetryClick = {
                        actions.onLoadNextPageOrRetry()
                    }
                )
            },
            onScrollToEnd = {
                actions.onLoadNextPageOrRetry()
            },
        )
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
            ImageFromNetwork(
                url = gif.mediumUrl,
                contentDescription = gif.title,
                thumbnailUrl = gif.thumbnailUrl
            )
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
                text = DateTimeUtils.format(gif.postedDatetime),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Row(
            modifier = Modifier.align(Alignment.End)
                .padding(bottom = 8.dp)
        ) {
            Text(stringResource(R.string.scroll_to_next))
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            Icon(
                Icons.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.scroll_to_next)
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
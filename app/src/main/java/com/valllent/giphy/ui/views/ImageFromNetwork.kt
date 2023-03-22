package com.valllent.giphy.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.skydoves.landscapist.glide.GlideImage
import com.valllent.giphy.R
import com.valllent.giphy.ui.wrappers.PreviewWrapper

@Composable
fun ImageFromNetwork(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    thumbnailUrl: String? = null,
) {
    val context = LocalContext.current
    val urlState = remember { mutableStateOf(url) }
    GlideImage(
        imageModel = { urlState.value },
        modifier = modifier.fillMaxSize(),
        loading = { LoadingPlaceholder() },
        failure = {
            FailurePlaceholder {
                val currentValue = urlState.value
                urlState.value = if (currentValue.endsWith("/")) {
                    currentValue.dropLast(1)
                } else {
                    "$currentValue/"
                }
            }
        },
        requestBuilder = {
            val thumbnailRequest = Glide
                .with(context)
                .load(thumbnailUrl)

            Glide
                .with(context)
                .asDrawable()
                .thumbnail(thumbnailRequest)
        }
    )
}

@Composable
private fun LoadingPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            Modifier.fillMaxSize(0.1f)
        )
    }
}

@Composable
private fun FailurePlaceholder(onRetryClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize(0.2f)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .clickable {
                        onRetryClick()
                    }
                    .align(Alignment.Center),
                imageVector = Icons.Filled.Refresh,
                contentDescription = stringResource(R.string.retry)
            )
        }
    }
}

@Preview()
@Composable
private fun LoadingPlaceholderPreview() {
    PreviewWrapper {
        LoadingPlaceholder()
    }
}

@Preview()
@Composable
private fun FailurePlaceholderPreview() {
    PreviewWrapper {
        FailurePlaceholder {}
    }
}
package com.valllent.giphy.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.valllent.giphy.ui.theme.ProjectTheme


@Composable
fun InProgress(
    modifier: Modifier = Modifier,
    fraction: Float = 0.2f
) {
    val size = remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size.value = it.size
            },
        contentAlignment = Alignment.Center
    ) {
        val heightIsSmaller = size.value.height < size.value.width
        CircularProgressIndicator(
            modifier = Modifier
                .let {
                    if (heightIsSmaller) {
                        it.fillMaxHeight(fraction)
                    } else {
                        it.fillMaxWidth(fraction)
                    }
                }
                .aspectRatio(1f, heightIsSmaller)
        )
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
private fun InProgressSquarePreview() {
    ProjectTheme {
        InProgress()
    }
}

@Preview(showBackground = true, widthDp = 150, heightDp = 300)
@Composable
private fun InProgressByHeightPreview() {
    ProjectTheme {
        InProgress()
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 150)
@Composable
private fun InProgressByWidthPreview() {
    ProjectTheme {
        InProgress()
    }
}
package com.valllent.giphy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valllent.giphy.ListOfGifsScreenViewModel
import com.valllent.giphy.R
import com.valllent.giphy.data.Gif
import com.valllent.giphy.ui.theme.ProjectTheme
import com.valllent.giphy.ui.views.ImageFromNetwork


@Composable
fun ListOfGifsScreen(
    onItemClick: ((Gif) -> Unit)? = null
) {
    val viewModel = viewModel<ListOfGifsScreenViewModel>()
    ListOfGifs(viewModel.gifs.value, onItemClick)
}

@Composable
private fun ListOfGifs(listOfGifs: List<Gif>, onItemClick: ((Gif) -> Unit)? = null) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn {
            items(
                listOfGifs,
                key = { gif -> gif.id }
            ) {
                GifItem(it, onItemClick)
            }
        }
    }
}


@Composable
private fun GifItem(
    gif: Gif,
    onItemClick: ((Gif) -> Unit)? = null
) {
    Column(
        Modifier.fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Surface(
            color = colorResource(R.color.gray_whiter),
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(90))
        ) {
            Text(
                gif.title,
                modifier = Modifier.padding(12.dp, 6.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 24.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(gif.width / gif.height.toFloat())
        ) {
            ImageFromNetwork(gif.originalUrl, gif.title, thumbnailUrl = gif.thumbnailUrl)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListOfGifsPreview() {
    ProjectTheme {
        ListOfGifs(
            listOf(
                Gif("id1", "title1", 2, 1, "originalUrl1", "previewUrl1"),
                Gif("id2", "title2", 3, 1, "originalUrl2", "previewUrl2"),
                Gif("id3", "title3", 1, 1, "originalUrl3", "previewUrl3"),
            )
        )
    }
}
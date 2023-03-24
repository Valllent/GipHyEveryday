package com.valllent.giphy.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valllent.giphy.R
import com.valllent.giphy.ui.wrappers.PreviewWrapper

@Composable
fun TitleOnSurface(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        color = colorResource(R.color.gray_whiter),
        modifier = modifier
            .clip(RoundedCornerShape(90))
            .let {
                if (onClick != null) {
                    it.clickable { onClick() }
                } else {
                    it
                }
            }
    ) {
        Text(
            text,
            modifier = Modifier.padding(12.dp, 6.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 24.sp
        )
    }
}

@Composable
fun SubtitleOnSurface(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        color = colorResource(R.color.teal_700),
        modifier = modifier
            .let {
                if (onClick != null) {
                    it.clickable { onClick() }
                } else {
                    it
                }
            }
    ) {
        Text(
            text,
            modifier = Modifier.padding(12.dp, 6.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 18.sp
        )
    }
}

@Composable
fun TitleWithSubtitleOnSurface(
    title: String,
    subtitle: String,
    onTitleClick: (() -> Unit)? = null,
    onSubtitleClick: (() -> Unit)? = null
) {
    Column {
        TitleOnSurface(title, onClick = onTitleClick)
        if (subtitle.isNotBlank()) {
            SubtitleOnSurface(
                text = subtitle,
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp)
                    .align(Alignment.End),
                onClick = onSubtitleClick
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TitleOnSurfacePreview() {
    PreviewWrapper {
        TitleOnSurface("Lorem ipsum dolor sit amet, consectetur adipiscing elit") {}
    }
}

@Preview(showBackground = true)
@Composable
fun SubtitleOnSurfacePreview() {
    PreviewWrapper {
        SubtitleOnSurface("Lorem ipsum dolor sit amet, consectetur adipiscing elit") {}
    }
}

@Preview(showBackground = true)
@Composable
fun TitleWithSubtitleOnSurfacePreview() {
    PreviewWrapper {
        TitleWithSubtitleOnSurface(
            "Title ipsum dolor sit amet, consectetur adipiscing elit",
            "Subtitle  ipsum dolor sit amet",
        )
    }
}
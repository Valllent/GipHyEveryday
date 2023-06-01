package com.valllent.giphy.app.presentation.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valllent.giphy.app.presentation.ui.wrappers.PreviewWrapper

@Composable
fun TitleOnSurface(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .shadow(4.dp, MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .let {
                if (onClick != null) {
                    it.clickable { onClick() }
                } else {
                    it
                }
            },
        color = backgroundColor,
        contentColor = textColor,
    ) {
        Text(
            text,
            modifier = Modifier.padding(12.dp, 6.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 24.sp,
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
        modifier = modifier
            .let {
                if (onClick != null) {
                    it.clickable { onClick() }
                } else {
                    it
                }
            },
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
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
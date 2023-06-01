package com.valllent.giphy.app.presentation.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valllent.giphy.R
import com.valllent.giphy.app.presentation.ui.wrappers.PreviewWrapper

@Composable
fun ProjectIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val shape = CircleShape

    Surface(
        modifier = modifier
            .padding(4.dp)
            .clip(shape)
            .border(
                BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.primary)),
                shape
            )
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Icon(
            imageVector,
            contentDescription,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewProjectIconButton() {
    PreviewWrapper {
        ProjectIconButton(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(id = R.string.menu)
        )
    }
}
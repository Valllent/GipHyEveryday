package com.valllent.giphy.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valllent.giphy.R

@Composable
fun Logo(modifier: Modifier = Modifier, fontSize: TextUnit = 28.sp) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier.padding(8.dp),
        shadowElevation = 10.dp,
        tonalElevation = 10.dp,
        color = MaterialTheme.colorScheme.primary,
    ) {
        Text(
            stringResource(R.string.app_name),
            modifier = Modifier.padding(16.dp),
            fontSize = fontSize,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLogo() {
    Logo()
}
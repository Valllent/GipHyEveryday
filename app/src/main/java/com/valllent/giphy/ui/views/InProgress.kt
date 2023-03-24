package com.valllent.giphy.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.valllent.giphy.ui.theme.ProjectTheme


@Composable
fun InProgress() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.2f))
    }
}

@Preview(showBackground = true)
@Composable
private fun InProgressPreview() {
    ProjectTheme {
        InProgress()
    }
}
package com.valllent.giphy.ui.wrappers

import androidx.compose.runtime.Composable
import com.valllent.giphy.ui.theme.ProjectTheme

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    ProjectTheme {
        content()
    }
}
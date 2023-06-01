package com.valllent.giphy.app.presentation.ui.wrappers

import androidx.compose.runtime.Composable
import com.valllent.giphy.app.presentation.ui.theme.ProjectTheme

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    ProjectTheme(useDynamicColors = false) {
        content()
    }
}
package com.valllent.giphy.app.presentation.ui.screens.detail

import androidx.compose.runtime.Stable

@Stable
data class DetailGifScreenActions(
    val onLoadNextPageOrRetry: () -> Unit,
    val onChangeSavedStateForGif: (id: String) -> Unit,
    val onReturnToPage: () -> Unit,
)
package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.runtime.Stable
import com.valllent.giphy.app.presentation.ui.screens.detail.OpenDetailScreenLambda

@Stable
data class SavedGifsActions(
    val onGifItemClick: OpenDetailScreenLambda,
    val onChangeSavedStateForGif: (String) -> Unit,
    val onLoadNextPagerOrRetry: () -> Unit,
)
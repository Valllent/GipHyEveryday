package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.runtime.Stable
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.screens.detail.OpenDetailScreenLambda

@Stable
data class SavedGifsActions(
    val onGifItemClick: OpenDetailScreenLambda,
    val onChangeSavedStateForGif: (GifUiModel) -> Unit,
    val onLoadNextPagerOrRetry: () -> Unit,
)
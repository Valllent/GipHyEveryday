package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.runtime.Stable
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.utils.OnGifClick

@Stable
data class SavedGifsActions(
    val onGifItemClick: OnGifClick,
    val onChangeSavedStateForGif: (GifUiModel) -> Unit,
)
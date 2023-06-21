package com.valllent.giphy.app.presentation.ui.screens.trending

import androidx.compose.runtime.Stable
import com.valllent.giphy.app.presentation.data.view.GifUiModel

@Stable
data class TrendingScreenActions(
    val onGifClick: (Int, GifUiModel) -> Unit,
    val onOpenSearch: () -> Unit,
    val onCloseSearch: () -> Unit,
    val onSearchClick: () -> Unit,
    val onSearchRequestChange: (String) -> Unit,
    val onChangeSavedStateForGif: (GifUiModel) -> Unit,
    val onSearchFieldFocusRequested: () -> Unit,
    val onLoadNextPage: () -> Unit,
)
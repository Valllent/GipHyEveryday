package com.valllent.giphy.app.presentation.ui.screens.trending

import androidx.compose.runtime.Stable
import com.valllent.giphy.app.presentation.ui.screens.detail.OpenDetailScreenLambda

@Stable
data class TrendingScreenActions(
    val onGifClick: OpenDetailScreenLambda,
    val onOpenSearch: () -> Unit,
    val onCloseSearch: () -> Unit,
    val onSearchClick: () -> Unit,
    val onSearchRequestChange: (String) -> Unit,
    val onChangeSavedStateForGif: (String) -> Unit,
    val onSearchFieldFocusRequested: () -> Unit,
    val onLoadNextPageOrRetry: () -> Unit,
    val onReturnToPage: () -> Unit,
)
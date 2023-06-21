package com.valllent.giphy.app.presentation.ui.pager

data class PagerListState<T>(
    val data: List<T>,
    val firstLoadingState: LoadingState,
    val appendLoadingState: LoadingState
)
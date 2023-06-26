package com.valllent.giphy.app.presentation.ui.screens.trending

import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.pager.PagerList
import kotlinx.coroutines.flow.StateFlow

data class TrendingScreenState(
    val gifs: StateFlow<PagerList<GifUiModel>>,
    val searchRequest: String,
    val searchRequestIsCorrect: Boolean,
    val showSearchField: Boolean,
    val showSearchResultList: Boolean,
    val searchFieldFocusRequestedAlready: Boolean
)
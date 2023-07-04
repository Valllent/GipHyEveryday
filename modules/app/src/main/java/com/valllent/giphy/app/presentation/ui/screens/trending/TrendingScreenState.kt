package com.valllent.giphy.app.presentation.ui.screens.trending

import com.valllent.giphy.app.presentation.ui.pager.PagerList
import com.valllent.giphy.domain.data.Gif
import kotlinx.coroutines.flow.StateFlow

data class TrendingScreenState(
    val gifs: StateFlow<PagerList<Gif>>,
    val searchRequest: String,
    val searchRequestIsCorrect: Boolean,
    val showSearchField: Boolean,
    val showSearchResultList: Boolean,
    val searchFieldFocusRequestedAlready: Boolean
)
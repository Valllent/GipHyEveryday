package com.valllent.giphy.app.presentation.ui.screens.trending

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import kotlinx.coroutines.flow.Flow

@Stable
data class TrendingScreenState(
    val currentGifsFlow: Flow<PagingData<GifUiModel>>,
    val searchRequest: String,
    val searchRequestIsCorrect: Boolean,
    val showSearchField: Boolean,
    val showSearchResultList: Boolean,
    val searchFieldFocusRequestedAlready: Boolean
)
package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.runtime.Stable
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.pager.PagerList
import kotlinx.coroutines.flow.StateFlow

@Stable
data class SavedGifsState(
    val pagerList: StateFlow<PagerList<GifUiModel>>
)
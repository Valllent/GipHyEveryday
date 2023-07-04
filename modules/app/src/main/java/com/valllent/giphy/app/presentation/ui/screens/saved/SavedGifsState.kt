package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.runtime.Stable
import com.valllent.giphy.app.presentation.ui.pager.PagerList
import com.valllent.giphy.domain.data.Gif
import kotlinx.coroutines.flow.StateFlow

@Stable
data class SavedGifsState(
    val pagerList: StateFlow<PagerList<Gif>>
)
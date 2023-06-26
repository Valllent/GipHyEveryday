package com.valllent.giphy.app.presentation.ui.screens.detail

import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.pager.PagerList
import kotlinx.coroutines.flow.StateFlow

data class DetailGifScreenState(
    val pagerListFlow: StateFlow<PagerList<GifUiModel>>,
    val currentItemIndex: Int,
)
package com.valllent.giphy.app.presentation.ui.screens.detail

import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.pager.PagerListState

data class DetailGifScreenState(
    val pagerList: PagerListState<GifUiModel>,
    val currentItemIndex: Int,
)
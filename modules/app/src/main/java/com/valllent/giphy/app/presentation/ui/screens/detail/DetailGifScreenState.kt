package com.valllent.giphy.app.presentation.ui.screens.detail

import com.valllent.giphy.app.presentation.ui.pager.PagerList
import com.valllent.giphy.domain.data.Gif
import kotlinx.coroutines.flow.StateFlow

data class DetailGifScreenState(
    val pagerListFlow: StateFlow<PagerList<Gif>>,
    val currentItemIndex: Int,
)
package com.valllent.giphy.app.presentation.ui.screens.detail

import androidx.paging.PagingData
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import kotlinx.coroutines.flow.Flow

data class DetailGifScreenState(
    val gifsFlow: Flow<PagingData<GifUiModel>>,
    val currentItemIndex: Int,
)
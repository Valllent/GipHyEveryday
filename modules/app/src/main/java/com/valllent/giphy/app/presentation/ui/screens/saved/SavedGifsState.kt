package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import kotlinx.coroutines.flow.Flow

@Stable
data class SavedGifsState(
    val gifsFlow: Flow<PagingData<GifUiModel>>
)
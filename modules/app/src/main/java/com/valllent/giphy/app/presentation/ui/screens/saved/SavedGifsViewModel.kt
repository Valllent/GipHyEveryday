package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.valllent.giphy.app.presentation.data.providers.GifPagingSource
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.screens.BaseViewModel
import com.valllent.giphy.app.presentation.ui.utils.Constants
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetSavedGifsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedGifsViewModel @Inject constructor(
    private val getSavedGifsUseCase: GetSavedGifsUseCase,
    private val changeSavedStateForGifUseCase: ChangeSavedStateForGifUseCase,
) : BaseViewModel() {

    private val pagingConfig = PagingConfig(pageSize = Constants.ITEMS_COUNT_PER_REQUEST)

    private val savedGifsFlow = Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            GifPagingSource { offset -> getSavedGifsUseCase(offset, Constants.ITEMS_COUNT_PER_REQUEST) }
        }
    ).flow.cachedIn(viewModelScope)

    private val _state = mutableStateOf(
        SavedGifsState(
            savedGifsFlow
        )
    )
    val state: State<SavedGifsState> = _state

    fun changeSavedState(gif: GifUiModel) {
        launchAsync {
            val currentState = changeSavedStateForGifUseCase(gif.id)
            gif.changeSavedState(currentState)
        }
    }

}
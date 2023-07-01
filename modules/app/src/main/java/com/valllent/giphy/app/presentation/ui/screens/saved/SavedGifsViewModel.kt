package com.valllent.giphy.app.presentation.ui.screens.saved

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.pager.PagerProvider
import com.valllent.giphy.app.presentation.ui.screens.BaseViewModel
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetSavedGifsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedGifsViewModel @Inject constructor(
    getSavedGifsUseCase: GetSavedGifsUseCase,
    private val pagerProvider: PagerProvider,
    private val changeSavedStateForGifUseCase: ChangeSavedStateForGifUseCase,
) : BaseViewModel() {

    private val gifsPager = pagerProvider.getSavedGifsPager(getSavedGifsUseCase)

    private val _state = mutableStateOf(
        SavedGifsState(
            gifsPager.state
        )
    )
    val state: State<SavedGifsState> = _state

    init {
        viewModelScope.launch {
            gifsPager.loadFirstPageIfNotYet()
        }
    }

    fun changeSavedState(gif: GifUiModel) {
        launchAsync {
            val currentState = changeSavedStateForGifUseCase(gif.id)
            gif.changeSavedState(currentState)
        }
    }

    fun loadNextPageOrRetry() {
        viewModelScope.launch {
            gifsPager.loadNextPage()
        }
    }

    override fun onCleared() {
        super.onCleared()
        pagerProvider.clearSavedGifsPager()
    }

}
package com.valllent.giphy.app.presentation.ui.screens.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.valllent.giphy.app.presentation.ui.ScreenArguments
import com.valllent.giphy.app.presentation.ui.pager.PagerProvider
import com.valllent.giphy.app.presentation.ui.screens.BaseViewModel
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetTrendingGifsUseCase
import com.valllent.giphy.domain.usecases.SearchGifsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailGifViewModel @Inject constructor(
    argumentsProvider: SavedStateHandle,
    pagerProvider: PagerProvider,
    getTrendingGifsUseCase: GetTrendingGifsUseCase,
    private val searchGifsUseCase: SearchGifsUseCase,
    private val changeSavedStateForGifUseCase: ChangeSavedStateForGifUseCase,
) : BaseViewModel() {

    private val pager = pagerProvider.getTrendingPager(getTrendingGifsUseCase)

    private val _state: MutableState<DetailGifScreenState> = mutableStateOf(
        DetailGifScreenState(
            pagerListFlow = pager.state,
            currentItemIndex = argumentsProvider.get<Int>(ScreenArguments.DETAIL_SCREEN_GIF_INDEX) ?: 0
        )
    )
    val state: State<DetailGifScreenState>
        get() = _state


    init {
        viewModelScope.launch {
            pager.loadFirstPageIfNotYet()
        }
    }

    fun loadNextPageOrRetryPrevious() {
        viewModelScope.launch {
            pager.loadNextPage()
        }
    }

}
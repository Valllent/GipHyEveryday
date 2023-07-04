package com.valllent.giphy.app.presentation.ui.screens.detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.valllent.giphy.app.presentation.data.providers.GifCustomPager
import com.valllent.giphy.app.presentation.ui.ScreenArguments
import com.valllent.giphy.app.presentation.ui.pager.PagerProvider
import com.valllent.giphy.app.presentation.ui.screens.BaseViewModel
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetSavedGifsUseCase
import com.valllent.giphy.domain.usecases.GetTrendingGifsUseCase
import com.valllent.giphy.domain.usecases.SearchGifsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailGifViewModel @Inject constructor(
    argumentsProvider: SavedStateHandle,
    pagerProvider: PagerProvider,
    getTrendingGifsUseCase: GetTrendingGifsUseCase,
    getSavedGifsUseCase: GetSavedGifsUseCase,
    searchGifsUseCase: SearchGifsUseCase,
    private val changeSavedStateForGifUseCase: ChangeSavedStateForGifUseCase,
) : BaseViewModel() {

    private val pager: GifCustomPager

    private val _state: MutableState<DetailGifScreenState>
    val state: State<DetailGifScreenState>
        get() = _state


    init {
        val gifIndex = argumentsProvider.get<Int>(ScreenArguments.DETAIL_SCREEN_GIF_INDEX) ?: 0
        val pagerTypeIndex = argumentsProvider.get<Int>(ScreenArguments.DETAIL_SCREEN_PAGER_TYPE_INDEX) ?: 0
        val searchRequest = argumentsProvider.get<String>(ScreenArguments.DETAIL_SCREEN_SEARCH_REQUEST) ?: "love"

        val arguments = OpenDetailScreenLambda.PagerType.values().first { it.ordinal == pagerTypeIndex }
        pager = when (arguments) {
            OpenDetailScreenLambda.PagerType.TRENDING -> {
                pagerProvider.getTrendingPager(getTrendingGifsUseCase)
            }

            OpenDetailScreenLambda.PagerType.SEARCH -> {
                pagerProvider.getSearchPager(searchGifsUseCase, searchRequest)
            }

            OpenDetailScreenLambda.PagerType.SAVED -> {
                pagerProvider.getSavedGifsPager(getSavedGifsUseCase)
            }
        }

        _state = mutableStateOf(
            DetailGifScreenState(
                pagerListFlow = pager.state,
                currentItemIndex = gifIndex
            )
        )

        launch {
            pager.loadFirstPageIfNotYet()
        }
    }

    fun loadNextPageOrRetryPrevious() {
        launch {
            pager.loadNextPage()
        }
    }

    fun changeSavedState(id: String) {
        launchAsync {
            val newIsSavedValue = changeSavedStateForGifUseCase(id)
            pager.changeSavedStateForGif(id, newIsSavedValue)
        }
    }

}
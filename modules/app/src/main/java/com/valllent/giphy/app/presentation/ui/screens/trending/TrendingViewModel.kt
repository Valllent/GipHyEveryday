package com.valllent.giphy.app.presentation.ui.screens.trending

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.pager.CustomPager
import com.valllent.giphy.app.presentation.ui.pager.PagerProvider
import com.valllent.giphy.app.presentation.ui.screens.BaseViewModel
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetTrendingGifsUseCase
import com.valllent.giphy.domain.usecases.SearchGifsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val pagerProvider: PagerProvider,
    private val getTrendingGifsUseCase: GetTrendingGifsUseCase,
    private val searchGifsUseCase: SearchGifsUseCase,
    private val changeSavedStateForGifUseCase: ChangeSavedStateForGifUseCase,
) : BaseViewModel() {

    private val trendingPager = pagerProvider.getTrendingPager(getTrendingGifsUseCase)
    private var searchPager: CustomPager<GifUiModel>? = null

    private var lastSearchJob: Job? = null

    private val _state = mutableStateOf(
        TrendingScreenState(
            trendingPager.state,
            "",
            searchRequestIsCorrect = false,
            showSearchField = false,
            showSearchResultList = false,
            searchFieldFocusRequestedAlready = false
        )
    )
    val state: State<TrendingScreenState>
        get() = _state


    init {
        viewModelScope.launch {
            trendingPager.loadFirstPageIfNotYet()
        }
    }


    fun changeSavedState(gifUiModel: GifUiModel) {
        launchAsync {
            val currentState = changeSavedStateForGifUseCase(gifUiModel.id)
            gifUiModel.changeSavedState(currentState)
        }
    }

    fun setSearchRequest(request: String) {
        _state.value = _state.value.copy(
            searchRequest = request,
            searchRequestIsCorrect = request.isNotBlank()
        )
    }

    fun search() {
        if (_state.value.searchRequestIsCorrect) {
            lastSearchJob?.cancel()

            lastSearchJob = viewModelScope.launch {
                searchPager = pagerProvider.getSearchPager(searchGifsUseCase, state.value.searchRequest)
                searchPager?.let {
                    it.loadFirstPageIfNotYet()

                    _state.value = _state.value.copy(
                        gifs = it.state,
                        showSearchResultList = true
                    )
                }
            }
        }
    }

    fun showSearchField() {
        _state.value = _state.value.copy(
            showSearchField = true
        )
    }

    fun hideSearchField() {
        searchPager = null
        _state.value = _state.value.copy(
            gifs = trendingPager.state,
            showSearchField = false,
            showSearchResultList = false,
            searchFieldFocusRequestedAlready = false,
        )
    }

    fun searchFieldFocusRequested() {
        _state.value = _state.value.copy(
            searchFieldFocusRequestedAlready = true
        )
    }

    fun loadNextPageOrRetryPrevious() {
        viewModelScope.launch {
            getCurrentPager().loadNextPage()
        }
    }

    private fun getCurrentPager(): CustomPager<GifUiModel> {
        val searchPager = searchPager
        if (state.value.showSearchResultList && searchPager != null) {
            return searchPager
        }

        return trendingPager
    }

}
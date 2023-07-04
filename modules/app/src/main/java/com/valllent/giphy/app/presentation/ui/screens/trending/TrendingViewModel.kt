package com.valllent.giphy.app.presentation.ui.screens.trending

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.valllent.giphy.app.presentation.data.providers.GifCustomPager
import com.valllent.giphy.app.presentation.ui.pager.PagerProvider
import com.valllent.giphy.app.presentation.ui.screens.BaseViewModel
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetTrendingGifsUseCase
import com.valllent.giphy.domain.usecases.SearchGifsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    getTrendingGifsUseCase: GetTrendingGifsUseCase,
    private val pagerProvider: PagerProvider,
    private val searchGifsUseCase: SearchGifsUseCase,
    private val changeSavedStateForGifUseCase: ChangeSavedStateForGifUseCase,
    private val getSavedStateForGifUseCase: GetSavedStateForGifUseCase,
) : BaseViewModel() {

    private val trendingPager = pagerProvider.getTrendingPager(getTrendingGifsUseCase)
    private var searchPager: GifCustomPager? = null

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
        launch {
            trendingPager.loadFirstPageIfNotYet()
        }
    }


    fun changeSavedState(id: String) {
        launchAsync {
            val newIsSavedValue = changeSavedStateForGifUseCase(id)
            getCurrentPager().changeSavedStateForGif(id, newIsSavedValue)
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

            lastSearchJob = launch {
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
        launch {
            getCurrentPager().loadNextPage()
        }
    }

    fun updateIsSavedValues() {
        launch {
            getCurrentPager().updateIsSavedValues(getSavedStateForGifUseCase)
        }
    }

    private fun getCurrentPager(): GifCustomPager {
        val searchPager = searchPager
        if (state.value.showSearchResultList && searchPager != null) {
            return searchPager
        }

        return trendingPager
    }

}
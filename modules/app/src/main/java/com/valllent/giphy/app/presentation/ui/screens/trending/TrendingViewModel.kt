package com.valllent.giphy.app.presentation.ui.screens.trending

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.valllent.giphy.app.presentation.data.providers.GifPagingSource
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.screens.BaseViewModel
import com.valllent.giphy.app.presentation.ui.utils.Constants
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetTrendingGifsUseCase
import com.valllent.giphy.domain.usecases.SearchGifsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val getTrendingGifsUseCase: GetTrendingGifsUseCase,
    private val searchGifsUseCase: SearchGifsUseCase,
    private val changeSavedStateForGifUseCase: ChangeSavedStateForGifUseCase,
) : BaseViewModel() {

    private val pagingConfig = PagingConfig(pageSize = Constants.ITEMS_COUNT_PER_REQUEST)

    private val trendingGifsFlow = Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            GifPagingSource { offset -> getTrendingGifsUseCase(offset, Constants.ITEMS_COUNT_PER_REQUEST) }
        }
    ).flow.cachedIn(viewModelScope)

    private var searchGifsFlow: Flow<PagingData<GifUiModel>>? = null


    private val _state = mutableStateOf(
        TrendingScreenState(
            trendingGifsFlow,
            "",
            searchRequestIsCorrect = false,
            showSearchField = false,
            showSearchResultList = false,
            searchFieldFocusRequestedAlready = false
        )
    )
    val state: State<TrendingScreenState>
        get() = _state


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
            val searchPagingSource = GifPagingSource { offset ->
                searchGifsUseCase(state.value.searchRequest, offset, Constants.ITEMS_COUNT_PER_REQUEST)
            }
            searchGifsFlow = Pager(
                config = pagingConfig,
                pagingSourceFactory = { searchPagingSource }
            ).flow.cachedIn(viewModelScope).apply {
                _state.value = _state.value.copy(
                    currentGifsFlow = this,
                    showSearchResultList = true
                )
            }
        }
    }

    fun showSearchField() {
        _state.value = _state.value.copy(
            showSearchField = true
        )
    }

    fun hideSearchField() {
        _state.value = _state.value.copy(
            currentGifsFlow = trendingGifsFlow,
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

}
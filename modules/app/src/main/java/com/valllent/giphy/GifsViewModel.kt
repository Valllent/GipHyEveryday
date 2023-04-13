package com.valllent.giphy

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.valllent.giphy.domain.data.Gif
import com.valllent.giphy.domain.usecases.GetTrendingGifs
import com.valllent.giphy.domain.usecases.SearchGifs
import com.valllent.giphy.ui.data.providers.GifPagingSource
import com.valllent.giphy.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GifsViewModel @Inject constructor(
    private val getTrendingGifs: GetTrendingGifs,
    private val searchGifs: SearchGifs,
) : BaseViewModel() {

    private val _searchRequest = MutableStateFlow("")
    val searchRequest = _searchRequest.asStateFlow()

    private val _searchRequestIsCorrect = MutableStateFlow(false)
    val searchRequestIsCorrect = _searchRequestIsCorrect.asStateFlow()

    private val pagingConfig = PagingConfig(pageSize = Constants.ITEMS_COUNT_PER_REQUEST)

    private val trendingGifsFlow = Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            GifPagingSource { offset -> getTrendingGifs(offset, Constants.ITEMS_COUNT_PER_REQUEST) }
        }
    ).flow.cachedIn(viewModelScope)

    private var searchGifsFlow: Flow<PagingData<Gif>>? = null

    private val _currentGifsFlow = MutableStateFlow(trendingGifsFlow)
    val currentGifsFlow = _currentGifsFlow.asStateFlow()


    fun setSearchRequest(request: String) {
        val requestIsCorrect = request.isNotBlank()
        _searchRequestIsCorrect.value = requestIsCorrect
        _searchRequest.value = request
    }

    fun search() {
        if (_searchRequestIsCorrect.value) {
            val searchPagingSource = GifPagingSource { offset ->
                searchGifs(_searchRequest.value, offset, Constants.ITEMS_COUNT_PER_REQUEST)
            }
            searchGifsFlow = Pager(
                config = pagingConfig,
                pagingSourceFactory = { searchPagingSource }
            ).flow.cachedIn(viewModelScope).apply {
                _currentGifsFlow.value = this
            }
        }
    }

    fun closeSearch() {
        _currentGifsFlow.value = trendingGifsFlow
    }

}
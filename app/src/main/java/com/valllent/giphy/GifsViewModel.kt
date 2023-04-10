package com.valllent.giphy

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.valllent.giphy.data.Gif
import com.valllent.giphy.network.GifNetworkDataSource
import com.valllent.giphy.ui.data.providers.GifPagingSource
import com.valllent.giphy.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GifsViewModel : BaseViewModel() {

    private val _searchRequest = MutableStateFlow("")
    val searchRequest = _searchRequest.asStateFlow()

    private val _showingSearchResult = MutableStateFlow(false)
    val showingSearchResult = _showingSearchResult.asStateFlow()

    private val _searchRequestIsCorrect = MutableStateFlow(false)
    val searchRequestIsCorrect = _searchRequestIsCorrect.asStateFlow()

    private val pagingConfig = PagingConfig(pageSize = Constants.ITEMS_COUNT_PER_REQUEST)

    private val trendingGifsFlow = Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            GifPagingSource { offset -> GifNetworkDataSource().getTrending(offset) }
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
                GifNetworkDataSource().search(_searchRequest.value, offset)
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
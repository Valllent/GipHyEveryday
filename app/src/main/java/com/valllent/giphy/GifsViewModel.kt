package com.valllent.giphy

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.valllent.giphy.data.Gif
import com.valllent.giphy.network.GifNetworkDataSource
import com.valllent.giphy.ui.dataproviders.GifPagingSource
import com.valllent.giphy.utils.Constants
import kotlinx.coroutines.delay

class GifsViewModel : BaseViewModel() {

    enum class DataFetchingStatus {
        IN_PROGRESS,
        FAILED,
        FETCHED,
    }

    private val _dataFetchingStatus = mutableStateOf(DataFetchingStatus.IN_PROGRESS)
    val dataFetchingStatus: State<DataFetchingStatus> = _dataFetchingStatus

    private val _gifs = mutableStateOf<List<Gif>>(emptyList())
    val gifs: State<List<Gif>> = _gifs

    val gifsFlow = Pager(
        config = PagingConfig(pageSize = Constants.ITEMS_COUNT_PER_REQUEST),
        pagingSourceFactory = { GifPagingSource() }
    ).flow.cachedIn(viewModelScope)

    init {
//        fetchGifs()
        _dataFetchingStatus.value = DataFetchingStatus.FETCHED
    }

    fun fetchGifs() {
        launch {
            _dataFetchingStatus.value = DataFetchingStatus.IN_PROGRESS
            delay(1_000)
            val result = GifNetworkDataSource().getTrending(0)
            if (result == null) {
                _dataFetchingStatus.value = DataFetchingStatus.FAILED
            } else {
                _gifs.value = result.gifs
                _dataFetchingStatus.value = DataFetchingStatus.FETCHED
            }
        }
    }

}
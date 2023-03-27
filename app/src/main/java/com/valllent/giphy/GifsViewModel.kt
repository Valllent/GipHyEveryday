package com.valllent.giphy

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.valllent.giphy.ui.dataproviders.GifPagingSource
import com.valllent.giphy.utils.Constants

class GifsViewModel : BaseViewModel() {

    enum class DataFetchingStatus {
        IN_PROGRESS,
        FAILED,
        FETCHED,
    }

    val gifsFlow = Pager(
        config = PagingConfig(pageSize = Constants.ITEMS_COUNT_PER_REQUEST),
        pagingSourceFactory = { GifPagingSource() }
    ).flow.cachedIn(viewModelScope)

}
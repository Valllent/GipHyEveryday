package com.valllent.giphy.ui.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.valllent.giphy.domain.data.Gif
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGif
import com.valllent.giphy.domain.usecases.GetSavedGifs
import com.valllent.giphy.ui.data.providers.GifPagingSource
import com.valllent.giphy.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SavedGifsViewModel @Inject constructor(
    private val getSavedGifs: GetSavedGifs,
    private val changeSavedStateForGif: ChangeSavedStateForGif,
) : BaseViewModel() {

    private val pagingConfig = PagingConfig(pageSize = Constants.ITEMS_COUNT_PER_REQUEST)

    private val trendingGifsFlow = Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            GifPagingSource { offset -> getSavedGifs(offset, Constants.ITEMS_COUNT_PER_REQUEST) }
        }
    ).flow.cachedIn(viewModelScope)

    private val _currentGifsFlow = MutableStateFlow(trendingGifsFlow)
    val currentGifsFlow = _currentGifsFlow.asStateFlow()

    suspend fun changeSavedState(gif: Gif): Boolean {
        val currentState = changeSavedStateForGif(gif.id)
        gif.isSaved = currentState
        return currentState
    }

}
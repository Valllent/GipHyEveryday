package com.valllent.giphy.app.presentation.ui.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.valllent.giphy.app.presentation.data.providers.GifPagingSource
import com.valllent.giphy.app.presentation.ui.utils.Constants
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetSavedGifsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SavedGifsViewModel @Inject constructor(
    private val getSavedGifsUseCase: GetSavedGifsUseCase,
    private val changeSavedStateForGifUseCase: ChangeSavedStateForGifUseCase,
) : BaseViewModel() {

    private val pagingConfig = PagingConfig(pageSize = Constants.ITEMS_COUNT_PER_REQUEST)

    private val trendingGifsFlow = Pager(
        config = pagingConfig,
        pagingSourceFactory = {
            GifPagingSource { offset -> getSavedGifsUseCase(offset, Constants.ITEMS_COUNT_PER_REQUEST) }
        }
    ).flow.cachedIn(viewModelScope)

    private val _currentGifsFlow = MutableStateFlow(trendingGifsFlow)
    val currentGifsFlow = _currentGifsFlow.asStateFlow()

    suspend fun changeSavedState(id: String): Boolean {
        launchAsync {
            val currentState = changeSavedStateForGifUseCase(id)

            val currentFlow = trendingGifsFlow

            currentFlow.map { pagingData ->
                pagingData.map { gif ->
                    if (gif.id == id) {
                        gif.changeSavedState(currentState)
                    }

                    false
                }

                false
            }
        }
        return false
    }

}
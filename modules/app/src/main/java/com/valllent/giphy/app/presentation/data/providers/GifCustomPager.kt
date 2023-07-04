package com.valllent.giphy.app.presentation.data.providers

import com.valllent.giphy.app.presentation.ui.pager.CustomPager
import com.valllent.giphy.domain.data.Gif
import com.valllent.giphy.domain.usecases.GetSavedStateForGifUseCase

class GifCustomPager(
    fetchData: suspend (Int) -> List<Gif>?
) : CustomPager<Gif>(fetchData) {

    fun changeSavedStateForGif(id: String, isSaved: Boolean) {
        modifyList(
            state.value.data.map {
                if (it.id == id) {
                    it.copy(isSaved = isSaved)
                } else {
                    it
                }
            }
        )
    }

    suspend fun updateIsSavedValues(getSavedStateForGifUseCase: GetSavedStateForGifUseCase) {
        modifyList(
            state.value.data.map {
                val currentValue = it.isSaved
                val newValue = getSavedStateForGifUseCase(it.id)

                if (currentValue != newValue) {
                    it.copy(isSaved = newValue)
                } else {
                    it
                }
            }
        )
    }

}
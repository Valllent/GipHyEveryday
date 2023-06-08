package com.valllent.giphy.domain.usecases

import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.domain.repositories.GifsNetworkRepository
import com.valllent.giphy.domain.repositories.SavedGifsDbRepository

class SearchGifsUseCase(
    private val gifsRepository: GifsNetworkRepository,
    private val savedGifsRepository: SavedGifsDbRepository,
) {

    suspend operator fun invoke(request: String, offset: Int, count: Int): GifPage? {
        val gifPage = gifsRepository.searchGifs(request, offset, count) ?: return null

        val gifsWithSavedFlag = gifPage.gifs.map {
            it.copy(isSaved = savedGifsRepository.isGifSaved(it.id))
        }

        return gifPage.copy(
            gifs = gifsWithSavedFlag
        )
    }

}
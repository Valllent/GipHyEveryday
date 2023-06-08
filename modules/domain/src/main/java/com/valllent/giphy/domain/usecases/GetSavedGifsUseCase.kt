package com.valllent.giphy.domain.usecases

import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.domain.repositories.GifsNetworkRepository
import com.valllent.giphy.domain.repositories.SavedGifsDbRepository

class GetSavedGifsUseCase(
    private val gifsRepository: GifsNetworkRepository,
    private val savedGifsRepository: SavedGifsDbRepository,
) {

    suspend operator fun invoke(offset: Int, count: Int): GifPage? {
        val idsOfSavedGifs = savedGifsRepository.getSavedGifIds(offset, count)
        val gifPage = gifsRepository.getGifsByIds(idsOfSavedGifs) ?: return null

        val gifsWithSavedFlag = gifPage.gifs.map { it.copy(isSaved = true) }
        return gifPage.copy(
            gifs = gifsWithSavedFlag
        )
    }

}
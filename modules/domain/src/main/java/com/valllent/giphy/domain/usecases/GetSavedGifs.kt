package com.valllent.giphy.domain.usecases

import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.domain.repositories.GifsRepository
import com.valllent.giphy.domain.repositories.SavedGifsRepository

class GetSavedGifs(
    private val gifsRepository: GifsRepository,
    private val savedGifsRepository: SavedGifsRepository,
) {

    suspend operator fun invoke(offset: Int, count: Int): GifPage? {
        val idsOfSavedGifs = savedGifsRepository.getSavedGifIds(offset, count)
        val gifPage = gifsRepository.getGifsByIds(idsOfSavedGifs)
        gifPage?.gifs?.forEach {
            it.isSaved = true
        }
        return gifPage
    }

}
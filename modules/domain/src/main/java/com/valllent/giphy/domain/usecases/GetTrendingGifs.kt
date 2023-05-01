package com.valllent.giphy.domain.usecases

import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.domain.repositories.GifsRepository
import com.valllent.giphy.domain.repositories.SavedGifsRepository

class GetTrendingGifs(
    private val gifsRepository: GifsRepository,
    private val savedGifsRepository: SavedGifsRepository,
) {

    suspend operator fun invoke(offset: Int, count: Int): GifPage? {
        val gifPage = gifsRepository.getTrendingGifs(offset, count)
        gifPage?.gifs?.forEach {
            it.isSaved = savedGifsRepository.isGifSaved(it.id)
        }
        return gifPage
    }

}
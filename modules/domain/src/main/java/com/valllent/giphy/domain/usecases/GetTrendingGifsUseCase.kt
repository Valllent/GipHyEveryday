package com.valllent.giphy.domain.usecases

import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.domain.repositories.GifsNetworkRepository
import com.valllent.giphy.domain.repositories.SavedGifsDbRepository

class GetTrendingGifsUseCase(
    private val gifsRepository: GifsNetworkRepository,
    private val savedGifsRepository: SavedGifsDbRepository,
) {

    suspend operator fun invoke(offset: Int, count: Int): GifPage? {
        val gifPage = gifsRepository.getTrendingGifs(offset, count) ?: return null

        val gifsWithSavedFlag = gifPage.gifs.toMutableList()
        for (i in 0 until gifsWithSavedFlag.size) {
            val gif = gifsWithSavedFlag[i]
            val isSaved = savedGifsRepository.isGifSaved(gif.id)
            if (isSaved) {
                gifsWithSavedFlag[i] = gif.copy(
                    isSaved = true
                )
            }
        }

        return gifPage.copy(
            gifs = gifsWithSavedFlag
        )
    }

}
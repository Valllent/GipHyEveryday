package com.valllent.giphy.domain.usecases

import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.domain.repositories.GifsRepository

class GetTrendingGifs(
    private val gifsRepository: GifsRepository
) {

    suspend operator fun invoke(offset: Int, count: Int): GifPage? {
        return gifsRepository.getTrendingGifs(offset, count)
    }

}
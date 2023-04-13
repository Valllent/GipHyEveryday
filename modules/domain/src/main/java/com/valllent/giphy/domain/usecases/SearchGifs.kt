package com.valllent.giphy.domain.usecases

import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.domain.repositories.GifsRepository

class SearchGifs(
    private val gifsRepository: GifsRepository
) {

    suspend operator fun invoke(request: String, offset: Int, count: Int): GifPage? {
        return gifsRepository.searchGifs(request, offset, count)
    }

}
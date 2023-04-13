package com.valllent.giphy.domain.repositories

import com.valllent.giphy.domain.data.GifPage

interface GifsRepository {

    suspend fun getTrendingGifs(
        offset: Int,
        count: Int
    ): GifPage?

    suspend fun searchGifs(
        request: String,
        offset: Int,
        count: Int
    ): GifPage?

}
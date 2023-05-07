package com.valllent.giphy.domain.repositories

import com.valllent.giphy.domain.data.GifPage

interface GifsRepository {

    suspend fun getGifsByIds(
        ids: List<String>
    ): GifPage?

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
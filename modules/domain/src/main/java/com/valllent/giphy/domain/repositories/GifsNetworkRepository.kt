package com.valllent.giphy.domain.repositories

import com.valllent.giphy.domain.data.GifPage

interface GifsNetworkRepository {

    suspend fun getGifsByIds(
        ids: List<String>,
        hasNextPage: Boolean,
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
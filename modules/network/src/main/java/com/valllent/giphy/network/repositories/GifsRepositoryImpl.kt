package com.valllent.giphy.network.repositories

import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.domain.repositories.GifsRepository
import com.valllent.giphy.network.api.GifsApi
import com.valllent.giphy.network.converters.GifConverter
import com.valllent.giphy.network.utils.CoroutineExtensions

class GifsRepositoryImpl(
    private val gifsApi: GifsApi
) : GifsRepository {

    override suspend fun getTrendingGifs(offset: Int, count: Int): GifPage? {
        val networkGifResponse = CoroutineExtensions.runSafely {
            gifsApi.getTrendingGifs(offset, count)
        }
        return GifConverter(networkGifResponse)
    }

    override suspend fun searchGifs(request: String, offset: Int, count: Int): GifPage? {
        val networkGifResponse = CoroutineExtensions.runSafely {
            gifsApi.search(request, offset, count)
        }
        return GifConverter(networkGifResponse)
    }

}
package com.valllent.giphy.network.repositories

import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.domain.repositories.GifsNetworkRepository
import com.valllent.giphy.network.api.GifsApi
import com.valllent.giphy.network.converters.GifConverter
import com.valllent.giphy.network.utils.CoroutineExtensions

class GifsNetworkRepositoryImpl(
    private val gifsApi: GifsApi
) : GifsNetworkRepository {

    override suspend fun getGifsByIds(ids: List<String>, hasNextPage: Boolean): GifPage? {
        val networkGifResponse = CoroutineExtensions.runSafely {
            val commaSeparatedIds = ids.joinToString(separator = ",")
            gifsApi.getGifsByIds(commaSeparatedIds)
        }
        return GifConverter(networkGifResponse)?.copy(hasNextPage = hasNextPage)
    }

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
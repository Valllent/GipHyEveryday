package com.valllent.giphy.network

import com.valllent.giphy.data.Gif
import com.valllent.giphy.data.GifPage
import com.valllent.giphy.network.data.responses.GifResponse
import com.valllent.giphy.utils.CoroutineExtensions.runSafely

class GifNetworkDataSource {

    suspend fun getTrending(offset: Int): GifPage? {
        val networkGifResponse = runSafely {
            NetworkModule.gifsApi.getTrendingGifs(offset)
        }
        return convertGifResponse(networkGifResponse)
    }

    suspend fun search(request: String, offset: Int): GifPage? {
        val networkGifResponse = runSafely {
            NetworkModule.gifsApi.search(request, offset)
        }
        return convertGifResponse(networkGifResponse)
    }


    private fun convertGifResponse(gifResponse: GifResponse?): GifPage? {
        if (gifResponse == null) return null

        val responseGifsList = gifResponse.gifsList
            ?: return GifPage(emptyList(), hasNextPage = false, fromNetwork = true)

        val gifs = ArrayList<Gif>(responseGifsList.size)
        responseGifsList.forEach { responseGif ->
            if (responseGif?.id == null) return@forEach

            gifs.add(
                Gif(
                    id = responseGif.id,
                    title = responseGif.title ?: "",
                    width = responseGif.urls?.originalUrl?.width ?: 100,
                    height = responseGif.urls?.originalUrl?.height ?: 100,
                    originalUrl = responseGif.urls?.originalUrl?.urlValue ?: "",
                    mediumUrl = responseGif.urls?.mediumUrl?.urlValue ?: "",
                    thumbnailUrl = responseGif.urls?.previewUrl?.urlValue ?: "",
                    postedBy = responseGif.username ?: "",
                    postedDatetime = responseGif.postedDatetime ?: ""
                )
            )
        }
        val totalCountOfItemsInCategory = gifResponse.pagination?.totalCount ?: 0
        val countOfAlreadyFetchedItems =
            (gifResponse.pagination?.offset ?: 0) + (gifResponse.pagination?.count ?: 0)
        val hasNextPage = (totalCountOfItemsInCategory - countOfAlreadyFetchedItems) > 0
        return GifPage(
            gifs = gifs,
            hasNextPage = hasNextPage,
            fromNetwork = true,
        )
    }

}
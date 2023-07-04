package com.valllent.giphy.network.converters

import com.valllent.giphy.domain.data.Gif
import com.valllent.giphy.domain.data.GifPage
import com.valllent.giphy.network.data.responses.GifResponse

object GifConverter {

    operator fun invoke(gifResponse: GifResponse?): GifPage? {
        if (gifResponse == null) return null

        val responseGifsList = gifResponse.gifsList
            ?: return GifPage(emptyList(), hasNextPage = false)

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
                    postedDatetime = responseGif.postedDatetime ?: "",
                    isSaved = false
                )
            )
        }
        val totalCountOfItemsInCategory = gifResponse.pagination?.totalCount ?: 0
        val countOfAlreadyFetchedItems = (gifResponse.pagination?.offset ?: 0) + (gifResponse.pagination?.count ?: 0)
        val hasNextPage = (totalCountOfItemsInCategory - countOfAlreadyFetchedItems) > 0
        return GifPage(
            gifs = gifs,
            hasNextPage = hasNextPage,
        )
    }

}
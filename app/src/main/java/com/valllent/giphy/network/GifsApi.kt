package com.valllent.giphy.network

import com.valllent.giphy.network.data.responses.GifResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GifsApi {

    @GET("/v1/gifs/trending?limit=6&rating=g")
    suspend fun getTrendingGifs(
        @Query("offset") offset: Int,
    ): GifResponse

}
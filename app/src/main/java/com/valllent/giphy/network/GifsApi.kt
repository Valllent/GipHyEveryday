package com.valllent.giphy.network

import com.valllent.giphy.network.data.responses.GifsResponse
import retrofit2.http.GET

interface GifsApi {

    @GET("/v1/gifs/trending?limit=6&rating=g")
    suspend fun getTrendingGifs(): GifsResponse

}
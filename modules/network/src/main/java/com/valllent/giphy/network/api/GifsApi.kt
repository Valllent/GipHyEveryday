package com.valllent.giphy.network.api

import com.valllent.giphy.network.data.responses.GifResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GifsApi {

    @GET("/v1/gifs?rating=r")
    suspend fun getGifsByIds(
        @Query("ids") ids: String
    ): GifResponse

    @GET("/v1/gifs/trending?rating=r")
    suspend fun getTrendingGifs(
        @Query("offset") offset: Int,
        @Query("limit") count: Int,
    ): GifResponse

    @GET("v1/gifs/search?rating=r")
    suspend fun search(
        @Query("q") request: String,
        @Query("offset") offset: Int,
        @Query("limit") count: Int
    ): GifResponse

}
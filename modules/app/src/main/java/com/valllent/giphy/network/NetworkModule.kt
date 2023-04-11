package com.valllent.giphy.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {

    private const val API_KEY = "FrH7U8B1VAy0v4siFTI5KEl2VL2XEQ8O"

    lateinit var gifsApi: GifsApi
        private set


    fun init() {
        val clientWithInterceptor = OkHttpClient.Builder()
            .addInterceptor(createInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(clientWithInterceptor)
            .baseUrl("https://api.giphy.com/")
            .build()

        gifsApi = retrofit.create(GifsApi::class.java)
    }

    private fun createInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()

            val newUrl = originalRequest.url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val newRequest: Request = originalRequest
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
    }

}
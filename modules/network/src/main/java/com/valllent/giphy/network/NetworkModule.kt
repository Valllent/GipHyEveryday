package com.valllent.giphy.network

import com.valllent.giphy.domain.repositories.GifsNetworkRepository
import com.valllent.giphy.network.api.GifsApi
import com.valllent.giphy.network.repositories.GifsNetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val API_KEY = "FrH7U8B1VAy0v4siFTI5KEl2VL2XEQ8O"
    }

    @Provides
    fun provideGifsRepository(gifsApi: GifsApi): GifsNetworkRepository {
        return GifsNetworkRepositoryImpl(gifsApi)
    }

    @Provides
    fun provideGifsApi(retrofit: Retrofit): GifsApi {
        return retrofit.create(GifsApi::class.java)
    }

    @Provides
    fun provideRetrofit(interceptor: Interceptor): Retrofit {
        val clientWithInterceptor = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(clientWithInterceptor)
            .baseUrl("https://api.giphy.com/")
            .build()
    }

    @Provides
    fun provideInterceptor(): Interceptor {
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
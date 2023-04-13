package com.valllent.giphy.di

import com.valllent.giphy.domain.repositories.GifsRepository
import com.valllent.giphy.domain.usecases.GetTrendingGifs
import com.valllent.giphy.domain.usecases.SearchGifs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {

    @Provides
    fun provideGetTrendingGifs(gifsRepository: GifsRepository): GetTrendingGifs {
        return GetTrendingGifs(gifsRepository)
    }

    @Provides
    fun provideSearchGifs(gifsRepository: GifsRepository): SearchGifs {
        return SearchGifs(gifsRepository)
    }

}
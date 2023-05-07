package com.valllent.giphy.di

import com.valllent.giphy.domain.repositories.GifsRepository
import com.valllent.giphy.domain.repositories.SavedGifsRepository
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGif
import com.valllent.giphy.domain.usecases.GetSavedGifs
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
    fun provideGetTrendingGifs(
        gifsRepository: GifsRepository,
        savedGifsRepository: SavedGifsRepository
    ): GetTrendingGifs {
        return GetTrendingGifs(gifsRepository, savedGifsRepository)
    }

    @Provides
    fun provideSearchGifs(gifsRepository: GifsRepository, savedGifsRepository: SavedGifsRepository): SearchGifs {
        return SearchGifs(gifsRepository, savedGifsRepository)
    }

    @Provides
    fun provideChangeSavedStateForGif(savedGifsRepository: SavedGifsRepository): ChangeSavedStateForGif {
        return ChangeSavedStateForGif(savedGifsRepository)
    }

    @Provides
    fun provideGetSavedGifs(gifsRepository: GifsRepository, savedGifsRepository: SavedGifsRepository): GetSavedGifs {
        return GetSavedGifs(gifsRepository, savedGifsRepository)
    }

}
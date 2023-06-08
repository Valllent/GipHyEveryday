package com.valllent.giphy.app.di

import com.valllent.giphy.domain.repositories.GifsNetworkRepository
import com.valllent.giphy.domain.repositories.SavedGifsDbRepository
import com.valllent.giphy.domain.usecases.ChangeSavedStateForGifUseCase
import com.valllent.giphy.domain.usecases.GetSavedGifsUseCase
import com.valllent.giphy.domain.usecases.GetTrendingGifsUseCase
import com.valllent.giphy.domain.usecases.SearchGifsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {

    @Provides
    fun provideGetTrendingGifs(
        gifsRepository: GifsNetworkRepository,
        savedGifsRepository: SavedGifsDbRepository
    ): GetTrendingGifsUseCase {
        return GetTrendingGifsUseCase(gifsRepository, savedGifsRepository)
    }

    @Provides
    fun provideSearchGifs(
        gifsRepository: GifsNetworkRepository,
        savedGifsRepository: SavedGifsDbRepository
    ): SearchGifsUseCase {
        return SearchGifsUseCase(gifsRepository, savedGifsRepository)
    }

    @Provides
    fun provideChangeSavedStateForGif(savedGifsRepository: SavedGifsDbRepository): ChangeSavedStateForGifUseCase {
        return ChangeSavedStateForGifUseCase(savedGifsRepository)
    }

    @Provides
    fun provideGetSavedGifs(
        gifsRepository: GifsNetworkRepository,
        savedGifsRepository: SavedGifsDbRepository
    ): GetSavedGifsUseCase {
        return GetSavedGifsUseCase(gifsRepository, savedGifsRepository)
    }

}
package com.valllent.giphy.app.di

import com.valllent.giphy.app.presentation.ui.pager.PagerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ApplicationModule {

    @Provides
    fun providePagerProvider(): PagerProvider {
        return PagerProvider.getInstance()
    }

}
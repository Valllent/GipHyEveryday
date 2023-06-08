package com.valllent.giphy.database

import android.content.Context
import androidx.room.Room
import com.valllent.giphy.database.daos.SavedGifDao
import com.valllent.giphy.database.databases.GiphyDatabase
import com.valllent.giphy.database.repositories.SavedGifsDbRepositoryImpl
import com.valllent.giphy.domain.repositories.SavedGifsDbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    companion object {
        private const val DATABASE_NAME = "GiphyDatabase"
    }

    @Provides
    fun provideSavedGifsRepository(savedGifDao: SavedGifDao): SavedGifsDbRepository {
        return SavedGifsDbRepositoryImpl(savedGifDao)
    }

    @Provides
    fun provideSavedGifsDao(database: GiphyDatabase): SavedGifDao {
        return database.getSavedGifDao()
    }

    @Provides
    fun provideGiphyDatabase(
        @ApplicationContext context: Context
    ): GiphyDatabase {
        return Room.databaseBuilder(
            context,
            GiphyDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

}
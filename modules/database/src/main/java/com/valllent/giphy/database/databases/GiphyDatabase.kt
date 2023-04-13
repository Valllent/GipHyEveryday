package com.valllent.giphy.database.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.valllent.giphy.database.daos.SavedGifDao
import com.valllent.giphy.database.tables.SavedGif

@Database(entities = [SavedGif::class], version = 1)
abstract class GiphyDatabase : RoomDatabase() {

    abstract fun getSavedGifDao(): SavedGifDao

}
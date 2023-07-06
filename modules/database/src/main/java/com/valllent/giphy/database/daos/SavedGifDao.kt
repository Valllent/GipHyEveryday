package com.valllent.giphy.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.valllent.giphy.database.tables.SavedGif

@Dao
interface SavedGifDao {

    @Query("SELECT COUNT(id) FROM savedGifs")
    fun getCount(): Int

    @Query("SELECT id FROM savedGifs LIMIT (:count) OFFSET (:offset)")
    fun getSavedGifs(offset: Int, count: Int): List<SavedGif>?

    @Query("SELECT id FROM savedGifs WHERE id=(:id)")
    fun getSavedGif(id: String): SavedGif?

    @Query("""INSERT INTO savedGifs (id) VALUES (:id)""")
    fun saveGif(id: String)

    @Query("""DELETE FROM savedGifs WHERE id=(:id)""")
    fun unsaveGif(id: String)

}
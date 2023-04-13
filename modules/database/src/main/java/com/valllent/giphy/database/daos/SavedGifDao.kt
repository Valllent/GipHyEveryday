package com.valllent.giphy.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.valllent.giphy.database.tables.SavedGif

@Dao
interface SavedGifDao {

    @Query("SELECT id FROM savedGifs WHERE id=(:id)")
    fun getSavedGif(id: String): SavedGif?

    @Query("""INSERT INTO savedGifs (id) VALUES (:id)""")
    fun saveGif(id: String)

    @Query("""DELETE FROM savedGifs WHERE id=(:id)""")
    fun unsaveGif(id: String)

}
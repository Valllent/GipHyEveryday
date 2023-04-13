package com.valllent.giphy.database.repositories

import com.valllent.giphy.database.daos.SavedGifDao
import com.valllent.giphy.domain.repositories.SavedGifsRepository

class SavedGifsRepositoryImpl(
    private val savedGifDao: SavedGifDao
) : SavedGifsRepository {

    override suspend fun changeSavedStateForGif(id: String) {
        val isAlreadySaved = savedGifDao.getSavedGif(id) != null
        if (isAlreadySaved) {
            savedGifDao.unsaveGif(id)
        } else {
            savedGifDao.saveGif(id)
        }
    }

}
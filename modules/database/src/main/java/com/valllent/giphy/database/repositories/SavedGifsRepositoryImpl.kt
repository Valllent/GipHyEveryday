package com.valllent.giphy.database.repositories

import com.valllent.giphy.database.daos.SavedGifDao
import com.valllent.giphy.domain.repositories.SavedGifsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SavedGifsRepositoryImpl(
    private val savedGifDao: SavedGifDao
) : SavedGifsRepository {

    override suspend fun getSavedGifIds(offset: Int, count: Int): List<String> {
        return withContext(Dispatchers.IO) {
            val savedGifs = savedGifDao.getSavedGifs(offset, count)
            savedGifs?.map { it.id } ?: emptyList()
        }
    }

    override suspend fun isGifSaved(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            savedGifDao.getSavedGif(id) != null
        }
    }

    override suspend fun changeSavedStateForGif(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            val isAlreadySaved = savedGifDao.getSavedGif(id) != null
            if (isAlreadySaved) {
                savedGifDao.unsaveGif(id)
            } else {
                savedGifDao.saveGif(id)
            }
            !isAlreadySaved
        }
    }

}
package com.valllent.giphy.domain.repositories


interface SavedGifsDbRepository {

    suspend fun getSavedGifIds(
        offset: Int,
        count: Int,
    ): List<String>

    suspend fun getSavedGifsCount(): Int

    suspend fun changeSavedStateForGif(
        id: String
    ): Boolean

    suspend fun isGifSaved(
        id: String
    ): Boolean

}
package com.valllent.giphy.domain.repositories


interface SavedGifsRepository {

    suspend fun getSavedGifIds(
        offset: Int,
        count: Int,
    ): List<String>

    suspend fun changeSavedStateForGif(
        id: String
    ): Boolean

    suspend fun isGifSaved(
        id: String
    ): Boolean

}
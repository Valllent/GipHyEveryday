package com.valllent.giphy.domain.repositories


interface SavedGifsRepository {

    suspend fun changeSavedStateForGif(
        id: String
    ): Boolean

    suspend fun isGifSaved(
        id: String
    ): Boolean

}
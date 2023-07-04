package com.valllent.giphy.domain.usecases

import com.valllent.giphy.domain.repositories.SavedGifsDbRepository

class GetSavedStateForGifUseCase(
    private val savedGifsRepository: SavedGifsDbRepository
) {

    suspend operator fun invoke(id: String): Boolean {
        return savedGifsRepository.isGifSaved(id)
    }

}
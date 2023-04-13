package com.valllent.giphy.domain.usecases

import com.valllent.giphy.domain.repositories.SavedGifsRepository

class ChangeSavedStateForGif(
    private val savedGifsRepository: SavedGifsRepository
) {

    suspend operator fun invoke(id: String) {
        savedGifsRepository.changeSavedStateForGif(id)
    }

}
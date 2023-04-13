package com.valllent.giphy.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "savedGifs",
)
data class SavedGif(
    @PrimaryKey val id: String
)
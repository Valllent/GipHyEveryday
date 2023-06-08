package com.valllent.giphy.app.presentation.data.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.valllent.giphy.domain.data.Gif
import java.util.*

@Stable
data class GifUiModel(
    val id: String,
    val title: String,
    val width: Int,
    val height: Int,
    val originalUrl: String,
    val mediumUrl: String,
    val thumbnailUrl: String,
    val postedBy: String,
    val postedDatetime: String,
    private val isSavedState: MutableState<Boolean>,
    val uniqueId: String = UUID.randomUUID().toString(),
) {

    companion object {

        fun from(gif: Gif): GifUiModel {
            return GifUiModel(
                id = gif.id,
                title = gif.title,
                width = gif.width,
                height = gif.height,
                originalUrl = gif.originalUrl,
                mediumUrl = gif.mediumUrl,
                thumbnailUrl = gif.thumbnailUrl,
                postedBy = gif.postedBy,
                postedDatetime = gif.postedDatetime,
                isSavedState = mutableStateOf(gif.isSaved)
            )
        }

    }

    val isSaved: State<Boolean>
        get() = isSavedState

    fun changeSavedState(newValue: Boolean) {
        isSavedState.value = newValue
    }

}
package com.valllent.giphy

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.valllent.giphy.data.Gif
import com.valllent.giphy.network.NetworkModule
import com.valllent.giphy.network.data.responses.GifsResponse
import kotlinx.coroutines.delay

class GifsViewModel : BaseViewModel() {

    enum class DataFetchingStatus {
        IN_PROGRESS,
        FAILED,
        FETCHED,
    }

    private val _dataFetchingStatus = mutableStateOf(DataFetchingStatus.IN_PROGRESS)
    val dataFetchingStatus: State<DataFetchingStatus> = _dataFetchingStatus

    private val _gifs = mutableStateOf<List<Gif>>(emptyList())
    val gifs: State<List<Gif>> = _gifs

    init {
        fetchGifs()
    }

    fun fetchGifs() {
        launch {
            _dataFetchingStatus.value = DataFetchingStatus.IN_PROGRESS
            delay(1_000)
            val result = runSafely {
                NetworkModule.gifsApi.getTrendingGifs()
            }
            if (result == null) {
                _dataFetchingStatus.value = DataFetchingStatus.FAILED
            } else {
                _gifs.value = convertGifResponse(result)
                _dataFetchingStatus.value = DataFetchingStatus.FETCHED
            }
        }
    }

    private fun convertGifResponse(gifsResponse: GifsResponse): List<Gif> {
        val responseGifsList = gifsResponse.gifsList ?: return emptyList()

        val gifs = ArrayList<Gif>(responseGifsList.size)
        responseGifsList.forEach { responseGif ->
            if (responseGif?.id == null) return@forEach

            gifs.add(
                Gif(
                    id = responseGif.id,
                    title = responseGif.title ?: "",
                    width = responseGif.urls?.originalUrl?.width ?: 100,
                    height = responseGif.urls?.originalUrl?.height ?: 100,
                    originalUrl = responseGif.urls?.originalUrl?.urlValue ?: "",
                    mediumUrl = responseGif.urls?.mediumUrl?.urlValue ?: "",
                    thumbnailUrl = responseGif.urls?.previewUrl?.urlValue ?: "",
                    postedBy = responseGif.username ?: "",
                    postedDatetime = responseGif.postedDatetime ?: ""
                )
            )
        }
        return gifs
    }

}
package com.valllent.giphy

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.valllent.giphy.data.Gif
import com.valllent.giphy.network.NetworkModule
import com.valllent.giphy.network.data.responses.GifsResponse
import kotlinx.coroutines.delay

class ListOfGifsScreenViewModel : BaseViewModel() {

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
        launch {
            val result = runSafely {
                NetworkModule.gifsApi.getTrendingGifs()
            }
            if (result == null) {
                _dataFetchingStatus.value = DataFetchingStatus.FAILED
            } else {
                _gifs.value = convertGifResponse(result)
                _dataFetchingStatus.value = DataFetchingStatus.FETCHED
            }

            delay(10_000)
            val gifs = ArrayList(_gifs.value)
            gifs.add(Gif("1", "title", 100, 100, "original", "preview"))
            _gifs.value = gifs
        }
    }

    private fun convertGifResponse(gifsResponse: GifsResponse): List<Gif> {
        val responseGifsList = gifsResponse.gifsList ?: return emptyList()

        val gifs = ArrayList<Gif>(responseGifsList.size)
        responseGifsList.forEach { responseGif ->
            if (responseGif?.id == null) return@forEach

            gifs.add(
                Gif(
                    responseGif.id,
                    responseGif.title ?: "",
                    responseGif.urls?.originalUrl?.width ?: 100,
                    responseGif.urls?.originalUrl?.height ?: 100,
                    responseGif.urls?.originalUrl?.urlValue ?: "",
                    responseGif.urls?.previewUrl?.urlValue ?: ""
                )
            )
        }
        return gifs
    }

}
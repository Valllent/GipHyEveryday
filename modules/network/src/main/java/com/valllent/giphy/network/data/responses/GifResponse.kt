package com.valllent.giphy.network.data.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valllent.giphy.network.data.other.GifItemResponse
import com.valllent.giphy.network.data.other.PaginationResponse

@JsonClass(generateAdapter = true)
data class GifResponse(
    @field:Json(name = "data") var gifsList: List<GifItemResponse?>?,
    @field:Json(name = "pagination") var pagination: PaginationResponse?,
) {

}
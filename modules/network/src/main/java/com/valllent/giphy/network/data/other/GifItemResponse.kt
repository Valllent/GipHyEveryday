package com.valllent.giphy.network.data.other

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GifItemResponse(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "username") val username: String?,
    @field:Json(name = "import_datetime") val postedDatetime: String?,
    @field:Json(name = "images") val urls: UrlsResponse?,
)

@JsonClass(generateAdapter = true)
data class UrlsResponse(
    @field:Json(name = "original") val originalUrl: UrlItemResponse?,
    @field:Json(name = "downsized") val mediumUrl: UrlItemResponse?,
    @field:Json(name = "preview_gif") val previewUrl: UrlItemResponse?,
)

@JsonClass(generateAdapter = true)
data class UrlItemResponse(
    @field:Json(name = "url") val urlValue: String?,
    @field:Json(name = "width") val width: Int?,
    @field:Json(name = "height") val height: Int?,
)
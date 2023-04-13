package com.valllent.giphy.network.data.other

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaginationResponse(
    @field:Json(name = "count") val count: Int?,
    @field:Json(name = "total_count") val totalCount: Int?,
    @field:Json(name = "offset") val offset: Int?
)
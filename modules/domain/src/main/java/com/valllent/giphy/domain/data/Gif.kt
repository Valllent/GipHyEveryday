package com.valllent.giphy.domain.data

data class Gif(
    val id: String,
    val title: String,
    val width: Int,
    val height: Int,
    val originalUrl: String,
    val mediumUrl: String,
    val thumbnailUrl: String,
    val postedBy: String,
    val postedDatetime: String,
    val isSaved: Boolean = false
)
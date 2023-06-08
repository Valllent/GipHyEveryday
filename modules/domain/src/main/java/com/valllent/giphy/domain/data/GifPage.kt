package com.valllent.giphy.domain.data

data class GifPage(
    val gifs: List<Gif>,
    val hasNextPage: Boolean,
)
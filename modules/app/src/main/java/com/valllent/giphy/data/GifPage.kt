package com.valllent.giphy.data

data class GifPage(
    val gifs: List<Gif>,
    val hasNextPage: Boolean,
    val fromNetwork: Boolean,
)
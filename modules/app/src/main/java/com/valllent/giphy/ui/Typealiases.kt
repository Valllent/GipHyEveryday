package com.valllent.giphy.ui

import com.valllent.giphy.domain.data.Gif

typealias Retry = () -> Unit

typealias OnGifClick = (Int, Gif) -> Unit
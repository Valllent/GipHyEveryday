package com.valllent.giphy.ui.preview

import com.valllent.giphy.data.Gif

object GifPreviewData {

    fun getList() = listOf(
        Gif(
            id = "id1",
            title = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
            width = 200, height = 100,
            originalUrl = "https://original-url.com/gif1",
            mediumUrl = "https://medium-url.com/gif1",
            thumbnailUrl = "https://thumbnail-url.com/gif1",
            postedBy = "Username1",
            postedDatetime = "2018-03-04 20:50:43"
        ),
        Gif(
            id = "id2",
            title = "Duis aute irure dolor in",
            width = 100, height = 150,
            originalUrl = "https://original-url.com/gif2",
            mediumUrl = "https://medium-url.com/gif2",
            thumbnailUrl = "https://thumbnail-url.com/gif2",
            postedBy = "Username2",
            postedDatetime = "1970-01-01 00:00:00"
        ),
        Gif(
            id = "id3",
            title = "Excepteur sint",
            width = 100, height = 150,
            originalUrl = "https://original-url.com/gif3",
            mediumUrl = "https://medium-url.com/gif3",
            thumbnailUrl = "https://thumbnail-url.com/gif3",
            postedBy = "Username3",
            postedDatetime = "1970-01-01 00:00:00"
        ),
    )

}
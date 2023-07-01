package com.valllent.giphy.app.presentation.ui.screens.detail

fun interface OpenDetailScreenLambda {

    fun run(arguments: Arguments)

    enum class PagerType {
        TRENDING,
        SAVED,
        SEARCH,
    }

    sealed class Arguments(
        val gifIndex: Int,
        val pagerTypeIndex: Int
    ) {

        class Trending(
            gifIndex: Int,
        ) : Arguments(gifIndex, PagerType.TRENDING.ordinal)

        class Saved(
            gifIndex: Int
        ) : Arguments(gifIndex, PagerType.SAVED.ordinal)

        class Search(
            gifIndex: Int,
            val searchRequest: String
        ) : Arguments(gifIndex, PagerType.SEARCH.ordinal)

    }

}
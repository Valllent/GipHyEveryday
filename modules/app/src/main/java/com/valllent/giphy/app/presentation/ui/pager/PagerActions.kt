package com.valllent.giphy.app.presentation.ui.pager

class PagerActions<T>(
    private val pager: CustomPager<T>
) {

    fun onLastPageLoaded() {
        pager.onLoadingLastPage()
    }

}
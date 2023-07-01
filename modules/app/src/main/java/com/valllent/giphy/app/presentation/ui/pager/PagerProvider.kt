package com.valllent.giphy.app.presentation.ui.pager

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.utils.Constants
import com.valllent.giphy.domain.usecases.GetSavedGifsUseCase
import com.valllent.giphy.domain.usecases.GetTrendingGifsUseCase
import com.valllent.giphy.domain.usecases.SearchGifsUseCase

class PagerProvider private constructor() : DefaultLifecycleObserver {

    companion object {

        private const val TAG = "PagerProvider"

        @Volatile
        private var INSTANCE: PagerProvider? = null

        fun getInstance(): PagerProvider {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = PagerProvider()
                    }
                }
            }
            return checkNotNull(INSTANCE)
        }
    }


    private var trendingPager: CustomPager<GifUiModel>? = null
    private var searchPager: CustomPager<GifUiModel>? = null
    private var savedGifsPager: CustomPager<GifUiModel>? = null

    private var lastSearchRequest: String? = null

    fun getTrendingPager(getTrendingGifsUseCase: GetTrendingGifsUseCase): CustomPager<GifUiModel> {
        if (trendingPager == null) {
            trendingPager = CustomPager { pageNumber ->
                getTrendingGifsUseCase(
                    pageNumber * Constants.ITEMS_COUNT_PER_REQUEST,
                    Constants.ITEMS_COUNT_PER_REQUEST
                )?.gifs?.map { GifUiModel.from(it) }
            }
        }

        return checkNotNull(trendingPager)
    }

    fun getSearchPager(searchGifsUseCase: SearchGifsUseCase, searchRequest: String): CustomPager<GifUiModel> {
        if (lastSearchRequest != searchRequest) searchPager = null

        if (searchPager == null) {
            searchPager = CustomPager { pageNumber ->
                searchGifsUseCase(
                    searchRequest,
                    pageNumber * Constants.ITEMS_COUNT_PER_REQUEST,
                    Constants.ITEMS_COUNT_PER_REQUEST
                )?.gifs?.map { GifUiModel.from(it) }
            }
            lastSearchRequest = searchRequest
        }

        return checkNotNull(searchPager)
    }

    fun getSavedGifsPager(getSavedGifsUseCase: GetSavedGifsUseCase): CustomPager<GifUiModel> {
        if (savedGifsPager == null) {
            savedGifsPager = CustomPager { pageNumber ->
                getSavedGifsUseCase(
                    pageNumber * Constants.ITEMS_COUNT_PER_REQUEST,
                    Constants.ITEMS_COUNT_PER_REQUEST
                )?.gifs?.map { GifUiModel.from(it) }
            }
        }

        return checkNotNull(savedGifsPager)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        destroy()
    }

    fun destroy() {
        trendingPager = null
        searchPager = null
        savedGifsPager = null
        lastSearchRequest = null

        Log.d(TAG, "PagerProvider cleaned.")
    }

}
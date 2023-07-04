package com.valllent.giphy.app.presentation.ui.pager

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.valllent.giphy.app.presentation.data.providers.GifCustomPager
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


    private var trendingPager: GifCustomPager? = null
    private var searchPager: GifCustomPager? = null
    private var savedGifsPager: GifCustomPager? = null

    private var lastSearchRequest: String? = null

    fun getTrendingPager(getTrendingGifsUseCase: GetTrendingGifsUseCase): GifCustomPager {
        if (trendingPager == null) {
            trendingPager = GifCustomPager { pageNumber ->
                getTrendingGifsUseCase(
                    pageNumber * Constants.ITEMS_COUNT_PER_REQUEST,
                    Constants.ITEMS_COUNT_PER_REQUEST
                )?.gifs
            }
        }

        return checkNotNull(trendingPager)
    }

    fun getSearchPager(searchGifsUseCase: SearchGifsUseCase, searchRequest: String): GifCustomPager {
        if (lastSearchRequest != searchRequest) searchPager = null

        if (searchPager == null) {
            searchPager = GifCustomPager { pageNumber ->
                searchGifsUseCase(
                    searchRequest,
                    pageNumber * Constants.ITEMS_COUNT_PER_REQUEST,
                    Constants.ITEMS_COUNT_PER_REQUEST
                )?.gifs
            }
            lastSearchRequest = searchRequest
        }

        return checkNotNull(searchPager)
    }

    fun getSavedGifsPager(getSavedGifsUseCase: GetSavedGifsUseCase): GifCustomPager {
        if (savedGifsPager == null) {
            savedGifsPager = GifCustomPager { pageNumber ->
                getSavedGifsUseCase(
                    pageNumber * Constants.ITEMS_COUNT_PER_REQUEST,
                    Constants.ITEMS_COUNT_PER_REQUEST
                )?.gifs ?: emptyList()
            }
        }

        return checkNotNull(savedGifsPager)
    }

    fun clearSavedGifsPager() {
        savedGifsPager = null
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
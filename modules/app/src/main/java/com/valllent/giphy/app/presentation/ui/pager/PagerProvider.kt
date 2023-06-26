package com.valllent.giphy.app.presentation.ui.pager

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.utils.Constants
import com.valllent.giphy.domain.usecases.GetTrendingGifsUseCase

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

    fun destroy() {
        Log.d(TAG, "PagerProvider cleaned.")
        trendingPager = null
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        destroy()
    }

}
package com.valllent.giphy.ui.data.providers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.valllent.giphy.domain.data.Gif
import com.valllent.giphy.domain.data.GifPage
import kotlinx.coroutines.delay

class GifPagingSource(
    private val fetchGifs: suspend (Int) -> GifPage?
) : PagingSource<Int, Gif>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        val pageNumber = params.key ?: 0
        val offset = params.loadSize

        // TODO: Remove
        if (pageNumber >= 1) {
            delay(5000)
        }

        val gifPage = fetchGifs(offset * pageNumber)
        return if (gifPage != null) {
            LoadResult.Page(
                gifPage.gifs,
                if (pageNumber == 0) null else pageNumber - 1,
                if (gifPage.hasNextPage) pageNumber + 1 else null
            )
        } else {
            LoadResult.Error(Exception("Can't download page: $pageNumber"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Gif>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            return anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}
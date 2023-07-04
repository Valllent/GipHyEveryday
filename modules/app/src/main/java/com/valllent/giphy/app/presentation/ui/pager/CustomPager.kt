package com.valllent.giphy.app.presentation.ui.pager

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

open class CustomPager<T>(
    private val fetchData: suspend (Int) -> List<T>?
) {

    companion object {
        const val TAG = "CustomPager"
    }

    private var pageNumber = 0
    private var firstLoading = true

    private val _state = MutableStateFlow<PagerList<T>>(
        PagerList(
            emptyList(),
            LoadingState.NOT_LOADING,
            LoadingState.NOT_LOADING,
        )
    )
    val state = _state.asStateFlow()

    private val dataList = mutableListOf<T>()

    /**
     * Load first data page.
     */
    suspend fun loadFirstPageIfNotYet() {
        if (state.value.data.isEmpty()) {
            loadNextPage()
        }
    }

    /**
     * Load next data page. It skips loading if it is in progress already.
     * Can be used instead of retry.
     */
    suspend fun loadNextPage() {
        // TODO: Add stop when no more new pages
        if (notInLoadingState()) {
            Log.d(TAG, "Loading next page...")
            flow {
                val result = runCatching { fetchData(pageNumber) }
                emit(result.getOrNull())
            }
                .onStart { setState(newLoadingState = LoadingState.LOADING) }
                .onCompletion { exception ->
                    val jobWasCancelled = exception != null
                    if (jobWasCancelled) {
                        setState(newLoadingState = LoadingState.NOT_LOADING)
                    }
                }
                .retry(2)
                .flowOn(Dispatchers.IO)
                .collect { newList ->
                    if (newList == null) {
                        setState(newLoadingState = LoadingState.FAILED)
                        return@collect
                    }

                    dataList.addAll(newList)
                    pageNumber++

                    setState(
                        newList = dataList,
                        newLoadingState = LoadingState.NOT_LOADING
                    )
                    firstLoading = false
                }
        }
    }

    fun modifyList(newList: List<T>) {
        setState(newList = newList)
    }

    private fun setState(
        newList: List<T>? = null,
        newLoadingState: LoadingState? = null,
    ) {

        if (newList != null && newLoadingState != null) {
            _state.value = if (firstLoading) {
                state.value.copy(
                    newList,
                    firstLoadingState = newLoadingState
                )
            } else {
                state.value.copy(
                    newList,
                    appendLoadingState = newLoadingState
                )
            }
            return
        }

        if (newList != null) {
            _state.value = state.value.copy(newList)
        }

        if (newLoadingState != null) {
            _state.value = if (firstLoading) {
                state.value.copy(
                    firstLoadingState = newLoadingState
                )
            } else {
                state.value.copy(
                    appendLoadingState = newLoadingState
                )
            }
        }
    }

    private fun notInLoadingState(): Boolean {
        return state.value.appendLoadingState != LoadingState.LOADING &&
                state.value.firstLoadingState != LoadingState.LOADING
    }

}
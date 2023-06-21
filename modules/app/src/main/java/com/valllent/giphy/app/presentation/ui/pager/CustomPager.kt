package com.valllent.giphy.app.presentation.ui.pager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class CustomPager<T>(
    private val fetchData: suspend (Int) -> List<T>?
) {

    private var pageNumber = 0

    private val _state = MutableStateFlow<PagerListState<T>>(
        PagerListState(
            emptyList(),
            LoadingState.NOT_LOADING,
            LoadingState.NOT_LOADING,
        )
    )
    val state = _state.asStateFlow()

    private val dataList = mutableListOf<T>()
    private var lastFlow: Flow<List<T>?>? = null

    /**
     * Requests next data page. It skips requesting if it is in progress already.
     * Can be used instead of retry.
     */
    suspend fun requestNextPage() {
        // TODO: Add stop when no more new pages
        if (notInLoadingState()) {
            lastFlow = flow {
                val result = runCatching { fetchData(pageNumber) }
                emit(result.getOrNull())
            }
                .onStart { setLoadingState(LoadingState.LOADING) }
                .retry(3)
                .flowOn(Dispatchers.IO)

            lastFlow?.collect { newList ->
                if (newList == null) {
                    setLoadingState(LoadingState.FAILED)
                    return@collect
                }

                dataList.addAll(newList)
                pageNumber++
                setLoadingState(LoadingState.NOT_LOADING)
                setState(
                    state.value.copy(
                        data = dataList,

                        )
                )
            }
        }
    }

    fun modifyData(modifiedList: List<T>) {
        setState(
            state.value.copy(
                data = modifiedList.toMutableList()
            )
        )
    }

    private fun setState(pagerList: PagerListState<T>) {
        _state.value = pagerList
    }

    private fun notInLoadingState(): Boolean {
        return state.value.appendLoadingState != LoadingState.LOADING &&
                state.value.firstLoadingState != LoadingState.LOADING
    }

    private fun setLoadingState(loadingState: LoadingState) {
        val firstLoading = state.value.data.isEmpty()

        if (firstLoading) {
            setState(
                state.value.copy(
                    firstLoadingState = loadingState
                )
            )
        } else {
            setState(
                state.value.copy(
                    appendLoadingState = loadingState
                )
            )
        }
    }

}
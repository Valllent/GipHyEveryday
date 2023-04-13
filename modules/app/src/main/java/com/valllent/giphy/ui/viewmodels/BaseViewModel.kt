package com.valllent.giphy.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valllent.giphy.utils.CoroutineExtensions
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    companion object {
        private const val TAG = "BaseViewModel"
    }

    init {
        Log.d(TAG, "Created new ViewModel: ${this.javaClass}")
    }

    protected val coroutineContext = CoroutineExceptionHandler { coroutineContext, throwable ->
        logError(throwable)
    }

    protected suspend fun <T> runSafely(code: suspend () -> T?): T? {
        return CoroutineExtensions.runSafely(code)
    }

    protected fun launch(code: suspend () -> Unit) {
        viewModelScope.launch(coroutineContext) {
            code()
        }
    }

    private fun logError(throwable: Throwable) {
        Log.e(TAG, "Exception in $this", throwable)
    }

}
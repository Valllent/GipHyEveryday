package com.valllent.giphy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    companion object {
        private const val TAG = "BaseViewModel"
    }

    protected val coroutineContext = CoroutineExceptionHandler { coroutineContext, throwable ->
        logError(throwable)
    }

    protected suspend fun <T> runSafely(code: suspend () -> T?): T? {
        val catchingResult = runCatching { code() }
        catchingResult.onFailure {
            logError(it)
        }
        return catchingResult.getOrNull()
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
package com.valllent.giphy.network.utils

import android.util.Log

object CoroutineExtensions {

    private const val TAG = "CoroutineExtensions"

    suspend fun <T> runSafely(code: suspend () -> T?): T? {
        val catchingResult = runCatching { code() }
        catchingResult.onFailure {
            Log.e(TAG, "Exception in coroutine: ", it)
        }
        return catchingResult.getOrNull()
    }

}


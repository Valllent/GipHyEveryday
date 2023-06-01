package com.valllent.giphy.app.presentation.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras

object MainActivityViewModelFactory {

    fun createViewModel(lifecycleOwner: ViewModelStoreOwner): MainActivityViewModel {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return MainActivityViewModel() as T
            }
        }
        return ViewModelProvider(lifecycleOwner, factory)[MainActivityViewModel::class.java]
    }

}
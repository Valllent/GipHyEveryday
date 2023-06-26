package com.valllent.giphy.app.presentation.activities

import com.valllent.giphy.app.presentation.ui.pager.PagerProvider
import com.valllent.giphy.app.presentation.ui.screens.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel : BaseViewModel() {

    private val _initializationInProgress = MutableStateFlow(false)
    val initializationInProgress = _initializationInProgress.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        PagerProvider.getInstance().destroy()
    }

}

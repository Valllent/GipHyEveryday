package com.valllent.giphy.app.presentation.ui.utils

import androidx.navigation.NavController
import com.valllent.giphy.app.presentation.data.view.DrawerItemUiModel
import com.valllent.giphy.app.presentation.data.view.GifUiModel

typealias Retry = () -> Unit

typealias OnGifClick = (Int, GifUiModel) -> Unit

typealias OnDrawerItemSelected = (DrawerItemUiModel) -> Unit

typealias GetNavigationController = () -> NavController
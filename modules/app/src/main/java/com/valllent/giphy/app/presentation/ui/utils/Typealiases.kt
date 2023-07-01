package com.valllent.giphy.app.presentation.ui.utils

import androidx.navigation.NavController
import com.valllent.giphy.app.presentation.data.view.DrawerItemUiModel

typealias Retry = () -> Unit

typealias OnDrawerItemSelected = (DrawerItemUiModel) -> Unit

typealias GetNavigationController = () -> NavController
package com.valllent.giphy.app.presentation.ui.utils

import androidx.navigation.NavController
import com.valllent.giphy.app.presentation.data.view.DrawerItem
import com.valllent.giphy.domain.data.Gif

typealias Retry = () -> Unit

typealias OnGifClick = (Int, Gif) -> Unit

typealias OnDrawerItemSelected = (DrawerItem) -> Unit

typealias GetNavigationController = () -> NavController
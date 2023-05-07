package com.valllent.giphy.ui.utils

import androidx.navigation.NavController
import com.valllent.giphy.domain.data.Gif
import com.valllent.giphy.ui.data.view.DrawerItem

typealias Retry = () -> Unit

typealias OnGifClick = (Int, Gif) -> Unit

typealias OnDrawerItemSelected = (DrawerItem) -> Unit

typealias GetNavigationController = () -> NavController
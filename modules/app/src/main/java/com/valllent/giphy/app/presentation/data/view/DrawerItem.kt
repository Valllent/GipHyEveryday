package com.valllent.giphy.app.presentation.data.view

import androidx.annotation.StringRes
import com.valllent.giphy.R
import com.valllent.giphy.app.presentation.ui.Screen

enum class DrawerItem(
    @StringRes val titleResId: Int,
    val screenRoute: String?
) {

    TRENDING(
        R.string.trending,
        Screen.ListOfGifs.staticRoute,
    ),
    SAVED(
        R.string.saved,
        Screen.ListOfSavedGifs.staticRoute
    ),
    SETTINGS(
        R.string.settings,
        null
    ),
    SEPARATOR(
        0,
        null
    );

    companion object {
        fun getInOrder() = arrayOf(
            SEPARATOR,
            TRENDING,
            SAVED,
            SEPARATOR,
            SETTINGS,
        )
    }

}
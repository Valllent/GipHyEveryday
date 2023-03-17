package com.valllent.giphy

import androidx.lifecycle.ViewModel
import com.valllent.giphy.data.User

class MainActivityViewModel : ViewModel() {

    val listOfUsers = listOf(
        User(
            "Valik",
            "Ukraine",
            16
        )
    )

}

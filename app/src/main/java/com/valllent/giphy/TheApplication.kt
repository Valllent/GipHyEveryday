package com.valllent.giphy

import android.app.Application
import com.valllent.giphy.network.NetworkModule

class TheApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        NetworkModule.init()
    }

}
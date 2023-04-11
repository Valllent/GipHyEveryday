package com.valllent.giphy.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.valllent.giphy.ui.wrappers.AppWrapper

class MainActivity : ComponentActivity() {

    private val viewModel by lazy { MainActivityViewModelFactory.createViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        showSplashScreen()
        super.onCreate(savedInstanceState)
        setContent { AppWrapper() }
    }

    private fun showSplashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
//            viewModel.initializationInProgress.value
            false
        }
    }

}
package com.valllent.giphy.app.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.valllent.giphy.app.presentation.ui.AppGraphHolder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by lazy { MainActivityViewModelFactory.createViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        showSplashScreen()
        super.onCreate(savedInstanceState)
        setContent { AppGraphHolder() }
    }

    private fun showSplashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
//            viewModel.initializationInProgress.value
            false
        }
    }

}
package com.valllent.giphy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.valllent.giphy.ui.AppWrapper

class MainActivity : ComponentActivity() {

    private val viewModel by lazy { ViewModelProvider(this)[MainActivityViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppWrapper(viewModel) }
    }

}
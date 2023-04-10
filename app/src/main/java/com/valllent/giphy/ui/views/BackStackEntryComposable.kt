package com.valllent.giphy.ui.views

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext


@Composable
fun BackStackEntryComposable(
    onBackPressedListener: () -> Unit
) {
    val backPressedDispatcher = (LocalContext.current as ComponentActivity).onBackPressedDispatcher
    val backListener = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedListener()
            }
        }
    }

    DisposableEffect(backPressedDispatcher) {
        backPressedDispatcher.addCallback(backListener)
        onDispose {
            backListener.remove()
        }
    }
}
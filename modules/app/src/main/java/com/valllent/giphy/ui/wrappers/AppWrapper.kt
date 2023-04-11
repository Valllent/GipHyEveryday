package com.valllent.giphy.ui.wrappers

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.valllent.giphy.GifsViewModel
import com.valllent.giphy.data.Gif
import com.valllent.giphy.ui.screens.DetailGifScreen
import com.valllent.giphy.ui.screens.ListOfGifsScreen
import com.valllent.giphy.ui.theme.ProjectTheme

sealed class Screen(private val route: String) {

    companion object {
        const val DETAIL_SCREEN_ARGUMENT_GIF_INDEX = "gifIndex"
    }

    object List : Screen("list/gif")

    object Detail : Screen("gif/{${DETAIL_SCREEN_ARGUMENT_GIF_INDEX}}") {
        fun createRoute(gifIndex: Int) = "gif/$gifIndex"
    }

    operator fun invoke(): String {
        return route
    }
}

@Composable
fun AppWrapper() {
    ProjectTheme {
        val navController = rememberNavController()
        val gifsViewModel = viewModel<GifsViewModel>()
        NavHost(navController, startDestination = Screen.List()) {
            List(
                gifsViewModel,
                onItemClick = { index, gif ->
                    navController.navigate(Screen.Detail.createRoute(index))
                }
            )
            Detail(gifsViewModel)
        }
    }
}

fun NavGraphBuilder.List(viewModel: GifsViewModel, onItemClick: (Int, Gif) -> Unit) {
    composable(route = Screen.List()) {
        ListOfGifsScreen(viewModel, onItemClick)
    }
}

fun NavGraphBuilder.Detail(viewModel: GifsViewModel) {
    composable(route = Screen.Detail()) {
        val selectedGifIndex =
            it.arguments?.getString(Screen.DETAIL_SCREEN_ARGUMENT_GIF_INDEX)?.toIntOrNull() ?: 0
        DetailGifScreen(viewModel, selectedGifIndex)
    }
}
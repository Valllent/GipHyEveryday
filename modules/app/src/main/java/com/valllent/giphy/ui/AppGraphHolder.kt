package com.valllent.giphy.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.valllent.giphy.domain.data.Gif
import com.valllent.giphy.ui.data.view.DrawerItem
import com.valllent.giphy.ui.screens.DetailGifScreen
import com.valllent.giphy.ui.screens.ListOfGifsScreen
import com.valllent.giphy.ui.screens.ListOfSavedGifsScreen
import com.valllent.giphy.ui.theme.ProjectTheme
import com.valllent.giphy.ui.utils.GetNavigationController
import com.valllent.giphy.ui.utils.OnDrawerItemSelected
import com.valllent.giphy.ui.viewmodels.GifsViewModel
import com.valllent.giphy.ui.viewmodels.SavedGifsViewModel

sealed class Screen(private val route: String) {

    companion object {
        const val DETAIL_SCREEN_ARGUMENT_GIF_INDEX = "gifIndex"
    }

    object ListOfGifs : Screen("list/gif")

    object ListOfSavedGifs : Screen("list/savedGif") {
        fun createRoute() = "list/savedGif"
    }

    object DetailGif : Screen("gif/{$DETAIL_SCREEN_ARGUMENT_GIF_INDEX}") {
        fun createRoute(gifIndex: Int) = "gif/$gifIndex"
    }

    object DetailSavedGif : Screen("savedGif/{$DETAIL_SCREEN_ARGUMENT_GIF_INDEX}") {
        fun createRoute(gifIndex: Int) = "savedGif/$gifIndex"
    }

    operator fun invoke(): String {
        return route
    }
}

class GlobalListeners(
    val onDrawerItemSelected: OnDrawerItemSelected,
    val getNavigationController: GetNavigationController,
)

@Composable
fun AppGraphHolder() {
    ProjectTheme {
        val navController = rememberNavController()

        val globalListeners = GlobalListeners(
            onDrawerItemSelected = createOnDrawerItemSelectedListener(navController),
            getNavigationController = { navController }
        )

        NavHost(navController, startDestination = Screen.ListOfGifs()) {
            ListOfGifs(
                onItemClick = { index, gif ->
                    navController.navigate(Screen.DetailGif.createRoute(index))
                },
                globalListeners
            )
            DetailGif(navController, globalListeners)

            ListOfSavedGifs(
                onItemClick = { index, gif ->
                    navController.navigate(Screen.DetailGif.createRoute(index))
                },
                globalListeners
            )
            DetailSavedGif(navController, globalListeners)
        }
    }
}

private fun createOnDrawerItemSelectedListener(navController: NavHostController): OnDrawerItemSelected {
    return { selectedDrawerItem ->
//        when (navController.currentBackStackEntry?.destination?.route) {
        when (selectedDrawerItem) {
            DrawerItem.SAVED -> {
                navController.navigate(Screen.ListOfSavedGifs.createRoute())
            }

            else -> {
                TODO()
            }
        }
    }
}


private fun NavGraphBuilder.ListOfGifs(onItemClick: (Int, Gif) -> Unit, globalListeners: GlobalListeners) {
    composable(route = Screen.ListOfGifs()) {
        val viewModel = hiltViewModel<GifsViewModel>()
        ListOfGifsScreen(viewModel, onItemClick, globalListeners)
    }
}

private fun NavGraphBuilder.DetailGif(navController: NavController, globalListeners: GlobalListeners) {
    composable(route = Screen.DetailGif()) {
        val selectedGifIndex = it.arguments?.getString(Screen.DETAIL_SCREEN_ARGUMENT_GIF_INDEX)?.toIntOrNull() ?: 0

        val backStackEntry = remember { checkNotNull(navController.previousBackStackEntry) }
        val viewModel = hiltViewModel<GifsViewModel>(backStackEntry)

        val flow = viewModel.currentGifsFlow.collectAsState().value
        DetailGifScreen(flow, selectedGifIndex, globalListeners)
    }
}

private fun NavGraphBuilder.ListOfSavedGifs(onItemClick: (Int, Gif) -> Unit, globalListeners: GlobalListeners) {
    composable(route = Screen.ListOfSavedGifs()) {
        val viewModel = hiltViewModel<SavedGifsViewModel>()
        ListOfSavedGifsScreen(viewModel, onItemClick, globalListeners)
    }
}

private fun NavGraphBuilder.DetailSavedGif(navController: NavController, globalListeners: GlobalListeners) {
    composable(route = Screen.DetailSavedGif()) {
        val selectedGifIndex = it.arguments?.getString(Screen.DETAIL_SCREEN_ARGUMENT_GIF_INDEX)?.toIntOrNull() ?: 0

        val backStackEntry = remember { checkNotNull(navController.previousBackStackEntry) }
        val viewModel = hiltViewModel<SavedGifsViewModel>(backStackEntry)

        val flow = viewModel.currentGifsFlow.collectAsState().value
        DetailGifScreen(flow, selectedGifIndex, globalListeners)
    }
}

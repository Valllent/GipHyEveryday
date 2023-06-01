package com.valllent.giphy.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
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

sealed class Screen(
    val staticRoute: String
) {

    companion object {
        const val DETAIL_SCREEN_ARGUMENT_GIF_INDEX = "gifIndex"
    }

    object ListOfGifs : Screen("gifs/trending") {

        fun createRoute() = staticRoute

        @Composable
        fun Content(onItemClick: (Int, Gif) -> Unit, globalListeners: GlobalListeners) {
            val viewModel = hiltViewModel<GifsViewModel>()
            ListOfGifsScreen(viewModel, onItemClick, globalListeners)
        }

    }

    object ListOfSavedGifs : Screen("gifs/saved") {

        fun createRoute() = staticRoute

        @Composable
        fun Content(onItemClick: (Int, Gif) -> Unit, globalListeners: GlobalListeners) {
            val viewModel = hiltViewModel<SavedGifsViewModel>()
            ListOfSavedGifsScreen(viewModel, onItemClick, globalListeners)
        }

    }

    object DetailGif : Screen("gifs/trending/{$DETAIL_SCREEN_ARGUMENT_GIF_INDEX}") {

        fun createRoute(gifIndex: Int) = "gifs/trending/$gifIndex"

        @Composable
        fun Content(
            navBackStackEntry: NavBackStackEntry,
            navController: NavController,
            globalListeners: GlobalListeners
        ) {
            val selectedGifIndex =
                navBackStackEntry.arguments?.getString(DETAIL_SCREEN_ARGUMENT_GIF_INDEX)?.toIntOrNull() ?: 0

            val backStackEntry = remember { checkNotNull(navController.previousBackStackEntry) }
            val viewModel = hiltViewModel<GifsViewModel>(backStackEntry)

            val flow = viewModel.currentGifsFlow.collectAsState().value
            DetailGifScreen(flow, selectedGifIndex, globalListeners)
        }

    }

    object DetailSavedGif : Screen("gifs/saved/{$DETAIL_SCREEN_ARGUMENT_GIF_INDEX}") {

        fun createRoute(gifIndex: Int) = "gifs/saved/$gifIndex"

        @Composable
        fun Content(
            navBackStackEntry: NavBackStackEntry,
            navController: NavController,
            globalListeners: GlobalListeners
        ) {
            val selectedGifIndex =
                navBackStackEntry.arguments?.getString(DETAIL_SCREEN_ARGUMENT_GIF_INDEX)?.toIntOrNull() ?: 0

            val backStackEntry = remember { checkNotNull(navController.previousBackStackEntry) }
            val viewModel = hiltViewModel<SavedGifsViewModel>(backStackEntry)

            val flow = viewModel.currentGifsFlow.collectAsState().value
            DetailGifScreen(flow, selectedGifIndex, globalListeners)
        }

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

        NavHost(navController, startDestination = Screen.ListOfGifs.staticRoute) {
            composable(Screen.ListOfGifs.staticRoute) {
                Screen.ListOfGifs.Content(
                    onItemClick = { index, gif ->
                        navController.navigate(Screen.DetailGif.createRoute(index))
                    },
                    globalListeners = globalListeners
                )
            }

            composable(Screen.DetailGif.staticRoute) {
                Screen.DetailGif.Content(it, navController, globalListeners)
            }

            composable(Screen.ListOfSavedGifs.staticRoute) {
                Screen.ListOfSavedGifs.Content(
                    onItemClick = { index, gif ->
                        navController.navigate(Screen.DetailSavedGif.createRoute(index))
                    },
                    globalListeners
                )
            }

            composable(Screen.DetailSavedGif.staticRoute) {
                Screen.DetailSavedGif.Content(it, navController, globalListeners)
            }
        }
    }
}

private fun createOnDrawerItemSelectedListener(navController: NavHostController): OnDrawerItemSelected {
    return { selectedDrawerItem ->

        when (selectedDrawerItem) {
            DrawerItem.TRENDING -> {
                navigateToExistingRouteOtherwiseCreateNewOne(Screen.ListOfGifs.createRoute(), navController)
            }

            DrawerItem.SAVED -> {
                navigateToExistingRouteOtherwiseCreateNewOne(Screen.ListOfSavedGifs.createRoute(), navController)
            }

            DrawerItem.SETTINGS -> {
                TODO()
            }

            DrawerItem.SEPARATOR -> {}
        }
    }
}

private fun navigateToExistingRouteOtherwiseCreateNewOne(
    destinationRoute: String,
    navController: NavController
) {
    val currentRoute = navController.currentDestination?.route
    if (currentRoute == destinationRoute) return

    val theSameDestinationInBackStack = navController.backQueue
        .map { it.destination }
        .firstOrNull { it.route == destinationRoute }

    if (theSameDestinationInBackStack != null) {
        navController.popBackStack(destinationRoute, false)
    } else {
        navController.navigate(destinationRoute)
    }
}

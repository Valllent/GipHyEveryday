package com.valllent.giphy.app.presentation.ui

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
import com.valllent.giphy.app.presentation.data.view.DrawerItemUiModel
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.screens.DetailGifScreen
import com.valllent.giphy.app.presentation.ui.screens.ListOfSavedGifsScreen
import com.valllent.giphy.app.presentation.ui.screens.trending.TrendingScreen
import com.valllent.giphy.app.presentation.ui.screens.trending.TrendingScreenActions
import com.valllent.giphy.app.presentation.ui.screens.trending.TrendingViewModel
import com.valllent.giphy.app.presentation.ui.theme.ProjectTheme
import com.valllent.giphy.app.presentation.ui.utils.GetNavigationController
import com.valllent.giphy.app.presentation.ui.utils.OnDrawerItemSelected
import com.valllent.giphy.app.presentation.ui.viewmodels.SavedGifsViewModel

sealed class Screen(
    val staticRoute: String
) {

    companion object {
        const val DETAIL_SCREEN_ARGUMENT_GIF_INDEX = "gifIndex"
    }

    object ListOfGifs : Screen("gifs/trending") {

        fun createRoute() = staticRoute

        @Composable
        fun Content(onItemClick: (Int, GifUiModel) -> Unit, globalListeners: GlobalListeners) {
            val viewModel = hiltViewModel<TrendingViewModel>()
            val state = viewModel.state.value
            val actions = TrendingScreenActions(
                onGifClick = { i, gif ->
                    onItemClick(i, gif)
                },
                onOpenSearch = {
                    viewModel.showSearchField()
                },
                onCloseSearch = {
                    viewModel.hideSearchField()
                },
                onSearchRequestChange = {
                    viewModel.setSearchRequest(it)
                },
                onSearchClick = {
                    viewModel.search()
                },
                onChangeSavedStateForGif = {
                    viewModel.changeSavedState(it)
                },
                onSearchFieldFocusRequested = {
                    viewModel.searchFieldFocusRequested()
                }
            )
            TrendingScreen(state, actions, globalListeners)
        }

    }

    object ListOfSavedGifs : Screen("gifs/saved") {

        fun createRoute() = staticRoute

        @Composable
        fun Content(onItemClick: (Int, GifUiModel) -> Unit, globalListeners: GlobalListeners) {
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
            val viewModel = hiltViewModel<TrendingViewModel>(backStackEntry)

            val flow = viewModel.state.value.currentGifsFlow
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
            DrawerItemUiModel.TRENDING -> {
                navigateToExistingRouteOtherwiseCreateNewOne(Screen.ListOfGifs.createRoute(), navController)
            }

            DrawerItemUiModel.SAVED -> {
                navigateToExistingRouteOtherwiseCreateNewOne(Screen.ListOfSavedGifs.createRoute(), navController)
            }

            DrawerItemUiModel.SETTINGS -> {
                TODO()
            }

            DrawerItemUiModel.SEPARATOR -> {}
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

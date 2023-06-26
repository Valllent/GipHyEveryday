package com.valllent.giphy.app.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.valllent.giphy.app.presentation.data.view.DrawerItemUiModel
import com.valllent.giphy.app.presentation.data.view.GifUiModel
import com.valllent.giphy.app.presentation.ui.screens.detail.DetailGifScreen
import com.valllent.giphy.app.presentation.ui.screens.detail.DetailGifScreenActions
import com.valllent.giphy.app.presentation.ui.screens.detail.DetailGifViewModel
import com.valllent.giphy.app.presentation.ui.screens.saved.SavedGifsActions
import com.valllent.giphy.app.presentation.ui.screens.saved.SavedGifsScreen
import com.valllent.giphy.app.presentation.ui.screens.saved.SavedGifsViewModel
import com.valllent.giphy.app.presentation.ui.screens.trending.TrendingScreen
import com.valllent.giphy.app.presentation.ui.screens.trending.TrendingScreenActions
import com.valllent.giphy.app.presentation.ui.screens.trending.TrendingViewModel
import com.valllent.giphy.app.presentation.ui.theme.ProjectTheme
import com.valllent.giphy.app.presentation.ui.utils.GetNavigationController
import com.valllent.giphy.app.presentation.ui.utils.OnDrawerItemSelected


object ScreenArguments {
    const val DETAIL_SCREEN_GIF_INDEX = "gifIndex"
}

sealed class Screen(
    val staticRoute: String
) {

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
                },
                onLoadNextPageOrRetry = {
                    viewModel.loadNextPageOrRetryPrevious()
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

            val state = viewModel.state.value
            val actions = SavedGifsActions(
                onGifItemClick = onItemClick,
                onChangeSavedStateForGif = {
                    viewModel.changeSavedState(it)
                }
            )
            SavedGifsScreen(state, actions, globalListeners)
        }

    }

    object DetailGif : Screen("gifs/trending/{${ScreenArguments.DETAIL_SCREEN_GIF_INDEX}}") {

        fun createRoute(gifIndex: Int) = "gifs/trending/$gifIndex"

        @Composable
        fun Content(
            globalListeners: GlobalListeners
        ) {
            val viewModel = hiltViewModel<DetailGifViewModel>()

            val state = viewModel.state.value
            val actions = DetailGifScreenActions(
                onLoadNextPageOrRetry = {
                    viewModel.loadNextPageOrRetryPrevious()
                }
            )

            DetailGifScreen(state, actions, globalListeners)
        }

    }

    object DetailSavedGif : Screen("gifs/saved/{${ScreenArguments.DETAIL_SCREEN_GIF_INDEX}}") {

        fun createRoute(gifIndex: Int) = "gifs/saved/$gifIndex"

        @Composable
        fun Content(
            navBackStackEntry: NavBackStackEntry,
            navController: NavController,
            globalListeners: GlobalListeners
        ) {
            val currentItemIndex =
                navBackStackEntry.arguments?.getString(ScreenArguments.DETAIL_SCREEN_GIF_INDEX)?.toIntOrNull() ?: 0

            val backStackEntry = remember { checkNotNull(navController.previousBackStackEntry) }
            val viewModel = hiltViewModel<SavedGifsViewModel>(backStackEntry)

            TODO()
//            val flow = viewModel.state.value.gifsFlow
//            val state = DetailGifScreenState(
//                pagerList = flow,
//                currentItemIndex = currentItemIndex
//            )
//            DetailGifScreen(state, globalListeners)
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

            composable(
                Screen.DetailGif.staticRoute,
                arguments = listOf(
                    navArgument(ScreenArguments.DETAIL_SCREEN_GIF_INDEX) {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) {
                Screen.DetailGif.Content(globalListeners)
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

package com.valllent.giphy.app.presentation.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.valllent.giphy.app.presentation.data.view.DrawerItemUiModel
import com.valllent.giphy.app.presentation.ui.screens.detail.DetailGifScreen
import com.valllent.giphy.app.presentation.ui.screens.detail.DetailGifScreenActions
import com.valllent.giphy.app.presentation.ui.screens.detail.DetailGifViewModel
import com.valllent.giphy.app.presentation.ui.screens.detail.OpenDetailScreenLambda
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
    const val DETAIL_SCREEN_SEARCH_REQUEST = "searchRequest"
    const val DETAIL_SCREEN_PAGER_TYPE_INDEX = "pagerTypeIndex"
}

sealed class Screen(
    val staticRoute: String
) {

    object ListOfGifs : Screen("gifs/trending") {

        fun createRoute() = staticRoute

        @Composable
        fun Content(onItemClick: OpenDetailScreenLambda, globalListeners: GlobalListeners) {
            val viewModel = hiltViewModel<TrendingViewModel>()
            val state = viewModel.state.value
            val actions = TrendingScreenActions(
                onGifClick = { arguments ->
                    onItemClick.run(arguments)
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
        fun Content(onItemClick: OpenDetailScreenLambda, globalListeners: GlobalListeners) {
            val viewModel = hiltViewModel<SavedGifsViewModel>()

            val state = viewModel.state.value
            val actions = SavedGifsActions(
                onGifItemClick = onItemClick,
                onChangeSavedStateForGif = {
                    viewModel.changeSavedState(it)
                },
                onLoadNextPagerOrRetry = {
                    viewModel.loadNextPageOrRetry()
                }
            )
            SavedGifsScreen(state, actions, globalListeners)
        }

    }

    object DetailGif : Screen(
        "gifs/detail/{${ScreenArguments.DETAIL_SCREEN_GIF_INDEX}}" +
                "?${ScreenArguments.DETAIL_SCREEN_PAGER_TYPE_INDEX}={${ScreenArguments.DETAIL_SCREEN_PAGER_TYPE_INDEX}}" +
                "&${ScreenArguments.DETAIL_SCREEN_SEARCH_REQUEST}={${ScreenArguments.DETAIL_SCREEN_SEARCH_REQUEST}}"
    ) {

        fun createRoute(arguments: OpenDetailScreenLambda.Arguments): String {
            return when (arguments) {
                is OpenDetailScreenLambda.Arguments.Search -> {
                    "gifs/detail/${arguments.gifIndex}" +
                            "?${ScreenArguments.DETAIL_SCREEN_PAGER_TYPE_INDEX}=${arguments.pagerTypeIndex}" +
                            "&${ScreenArguments.DETAIL_SCREEN_SEARCH_REQUEST}=${arguments.searchRequest}"
                }

                else -> {
                    "gifs/detail/${arguments.gifIndex}" +
                            "?${ScreenArguments.DETAIL_SCREEN_PAGER_TYPE_INDEX}=${arguments.pagerTypeIndex}"
                }
            }
        }


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
                    onItemClick = { arguments ->
                        navController.navigate(
                            Screen.DetailGif.createRoute(arguments)
                        )
                    },
                    globalListeners = globalListeners
                )
            }

            composable(Screen.ListOfSavedGifs.staticRoute) {
                Screen.ListOfSavedGifs.Content(
                    onItemClick = { arguments ->
                        navController.navigate(Screen.DetailGif.createRoute(arguments))
                    },
                    globalListeners
                )
            }

            composable(
                Screen.DetailGif.staticRoute,
                arguments = listOf(
                    navArgument(ScreenArguments.DETAIL_SCREEN_GIF_INDEX) {
                        type = NavType.IntType
                        defaultValue = 0
                    },
                    navArgument(ScreenArguments.DETAIL_SCREEN_PAGER_TYPE_INDEX) {
                        type = NavType.IntType
                        defaultValue = 0
                    },
                    navArgument(ScreenArguments.DETAIL_SCREEN_SEARCH_REQUEST) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) {
                Screen.DetailGif.Content(globalListeners)
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

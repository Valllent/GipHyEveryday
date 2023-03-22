package com.valllent.giphy.ui.wrappers

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.valllent.giphy.activities.main.MainActivityViewModel
import com.valllent.giphy.data.Gif
import com.valllent.giphy.ui.screens.ListOfGifsScreen

sealed class Screen(private val route: String) {
    object List : Screen("user/list") {
        fun routeToDetail(userId: String): String {
            return Detail.createRoute(userId)
        }
    }

    object Detail : Screen("user/{userId}") {
        fun createRoute(userId: String) = "user/$userId"
    }

    operator fun invoke(): String {
        return route
    }
}

@Composable
fun AppWrapper(viewModel: MainActivityViewModel) {
    ScaffoldWrapper {
        val navController = rememberNavController()
        NavHost(navController, startDestination = Screen.List()) {
            List(
                viewModel,
                onItemClick = { navController.navigate(Screen.List.routeToDetail(it.id.toString())) }
            )
            Detail(viewModel)
        }
    }
}

fun NavGraphBuilder.List(viewModel: MainActivityViewModel, onItemClick: (Gif) -> Unit) {
    composable(route = Screen.List()) {
        ListOfGifsScreen()
    }
}

fun NavGraphBuilder.Detail(viewModel: MainActivityViewModel) {
    composable(route = Screen.Detail()) { navStack ->
        val userId = navStack.arguments?.getString("userId")?.toIntOrNull()!!
//        UserDetailScreen(viewModel.listOfUsers.firstOrNull { it.id == userId }!!)
    }
}
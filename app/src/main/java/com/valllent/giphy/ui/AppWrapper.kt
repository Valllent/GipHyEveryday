package com.valllent.giphy.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.valllent.giphy.MainActivityViewModel
import com.valllent.giphy.data.User
import com.valllent.giphy.ui.screens.UserDetailScreen
import com.valllent.giphy.ui.screens.UserListScreen
import com.valllent.giphy.ui.views.ScreenWrapper

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
    ScreenWrapper {
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

fun NavGraphBuilder.List(viewModel: MainActivityViewModel, onItemClick: (User) -> Unit) {
    composable(route = Screen.List()) {
        UserListScreen(viewModel.listOfUsers, onItemClick)
    }
}

fun NavGraphBuilder.Detail(viewModel: MainActivityViewModel) {
    composable(route = Screen.Detail()) { navStack ->
        val userId = navStack.arguments?.getString("userId")?.toIntOrNull()!!
        UserDetailScreen(viewModel.listOfUsers.firstOrNull { it.id == userId }!!)
    }
}
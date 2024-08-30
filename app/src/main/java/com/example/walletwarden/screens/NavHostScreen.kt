package com.example.walletwarden.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.walletwarden.utils.isUserDataAvailable
import com.example.walletwarden.viewmodels.HomeViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object MonthExpense : Screen("month_expense/{monthId}") {
        fun createRoute(monthId: Int) = "month_expense/$monthId"
    }
}

@Composable
fun NavHostScreen(navController: NavHostController,homeViewModel: HomeViewModel,context: Context) {
    val startDestination = if (isUserDataAvailable(context)) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(Screen.Home.route) {
            HomeScreen(navController,homeViewModel)
        }
        composable(
            route = Screen.MonthExpense.route,
            arguments = listOf(navArgument("monthId") { type = NavType.IntType })
        ) { backStackEntry ->
            val monthId = backStackEntry.arguments?.getInt("monthId")
            monthId?.let { MonthScreen(navController, it,homeViewModel) }
        }
    }
}
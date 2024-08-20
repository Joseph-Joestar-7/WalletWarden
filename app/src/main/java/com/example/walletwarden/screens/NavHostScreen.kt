package com.example.walletwarden.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MonthExpense : Screen("month_expense/{monthId}") {
        fun createRoute(monthId: Int) = "month_expense/$monthId"
    }
}

@Composable
fun NavHostScreen(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = Screen.MonthExpense.route,
            arguments = listOf(navArgument("monthId") { type = NavType.IntType })
        ) { backStackEntry ->
            val monthId = backStackEntry.arguments?.getInt("monthId")
            monthId?.let { MonthScreen(navController, it) }
        }
    }
}
package com.example.nanitbirthdayapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nanitbirthdayapp.ui.birthday.BirthdayScreen
import com.example.nanitbirthdayapp.ui.birthday.BirthdayViewModel
import com.example.nanitbirthdayapp.ui.main.ConnectionScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    ) {
    val birthdayViewModel = hiltViewModel<BirthdayViewModel>()

    NavHost(
        navController = navController,
        startDestination = Screen.ConnectionScreen.route
    ) {
        composable(route = Screen.ConnectionScreen.route) {
            ConnectionScreen(
                viewModel = birthdayViewModel,
                navController = navController
            )
        }
        composable(route = Screen.BirthdayScreen.route) {
            BirthdayScreen(
                viewModel = birthdayViewModel,
            )
        }
    }
}

package com.example.nanitbirthdayapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val birthdayInfo by birthdayViewModel.birthdayInfo.collectAsState()
    val isConnected = birthdayInfo != null

    NavHost(
        navController = navController,
        startDestination = if (isConnected) Screen.BirthdayScreen.route else Screen.ConnectionScreen.route
    ) {
        composable(route = Screen.ConnectionScreen.route) {
            ConnectionScreen(
                viewModel = birthdayViewModel,
                onConnectClick = { ip, port ->
                    birthdayViewModel.connectToServer(ip, port)
                }
            )
        }
        composable(route = Screen.BirthdayScreen.route) {
            BirthdayScreen(
                viewModel = birthdayViewModel,
                onAddPictureClick = { uri ->
                    birthdayViewModel.updatePicture(uri)
                }
            )
        }
    }
}

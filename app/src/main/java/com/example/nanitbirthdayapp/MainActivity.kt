package com.example.nanitbirthdayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nanitbirthdayapp.ui.birthday.BirthdayScreen
import com.example.nanitbirthdayapp.ui.birthday.BirthdayViewModel
import com.example.nanitbirthdayapp.ui.main.ConnectionScreen
import com.example.nanitbirthdayapp.ui.theme.NanitBirthdayAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContent {
            NanitBirthdayAppTheme {
                val birthdayViewModel: BirthdayViewModel = hiltViewModel()
                val uiState by birthdayViewModel.uiState.collectAsState()

                if (uiState.birthdayInfo == null) {
                    ConnectionScreen(
                        uiState = uiState,
                        onConnectClick = { ip, port ->
                            birthdayViewModel.connectToServer(ip, port)
                        }
                    )
                } else {
                    BirthdayScreen(
                        birthdayInfo = uiState.birthdayInfo!!,
                        imageUri = uiState.selectedImageUri,
                        onAddPictureClick = {},
                        viewModel = birthdayViewModel
                    )
                }
            }
        }
    }
}

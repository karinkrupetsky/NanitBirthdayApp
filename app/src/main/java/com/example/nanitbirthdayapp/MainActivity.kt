package com.example.nanitbirthdayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nanitbirthdayapp.ui.birthday.BirthdayScreen
import com.example.nanitbirthdayapp.ui.main.ConnectionScreen
import com.example.nanitbirthdayapp.ui.main.MainViewModel
import com.example.nanitbirthdayapp.ui.theme.NanitBirthdayAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NanitBirthdayAppTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()

                if (uiState.birthdayInfo == null) {
                    ConnectionScreen(
                        uiState = uiState,
                        onConnectClick = { ip, port ->
                            viewModel.connectToServer(ip, port)
                        }
                    )
                } else {
                    BirthdayScreen(
                        birthdayInfo = uiState.birthdayInfo!!,
                        imageUri = uiState.imageUri,
                        onAddPictureClick = {},
                    )
                }
            }
        }
    }
}

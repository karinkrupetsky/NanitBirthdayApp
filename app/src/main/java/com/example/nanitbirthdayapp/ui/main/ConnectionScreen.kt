package com.example.nanitbirthdayapp.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nanitbirthdayapp.R
import com.example.nanitbirthdayapp.ui.birthday.BirthdayViewModel
import com.example.nanitbirthdayapp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionScreen(
    viewModel: BirthdayViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    var ipAddress by remember { mutableStateOf("192.168.1") }
    var port by remember { mutableStateOf("8080") }

    LaunchedEffect(uiState.birthdayInfo) {
        if (uiState.birthdayInfo != null) {
            navController.navigate(Screen.BirthdayScreen.route) {
                popUpTo(Screen.ConnectionScreen.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.connect_to_server), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = ipAddress,
            onValueChange = { ipAddress = it },
            label = { Text(stringResource(R.string.ip_address)) },
            isError = uiState.errorMessage != null
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = port,
            onValueChange = { port = it },
            label = { Text(stringResource(R.string.port)) },
            isError = uiState.errorMessage != null
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = { viewModel.connectToServer(ipAddress, port) }) {
                Text(stringResource(R.string.connect))
            }
        }

        uiState.errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}

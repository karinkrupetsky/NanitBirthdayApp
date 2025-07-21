package com.example.nanitbirthdayapp.ui.birthday

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nanitbirthdayapp.R
import com.example.nanitbirthdayapp.ui.birthday.constants.BirthdayConst
import com.example.nanitbirthdayapp.ui.components.PhotoPickerDialog
import com.example.nanitbirthdayapp.ui.theme.getAgeNumberDrawable
import com.example.nanitbirthdayapp.ui.theme.getBackgroundImageResource
import com.example.nanitbirthdayapp.ui.theme.getThemeBackgroundColor
import com.example.nanitbirthdayapp.ui.theme.shareBtnBg
import com.example.nanitbirthdayapp.util.AgeCalculator

@Composable
fun BirthdayScreen(
    viewModel: BirthdayViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var showPhotoPicker by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val graphicsLayer = rememberGraphicsLayer()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            }
            .background(
                uiState.birthdayInfo?.theme?.getThemeBackgroundColor()
                    ?: MaterialTheme.colorScheme.background
            )
    ) {
        uiState.birthdayInfo?.let { birthdayData ->
            BirthdayContent(
                uiState = uiState,
                onShowPhotoPicker = { showPhotoPicker = true },
                onShareClick = {
                    viewModel.shareBirthday {
                        try {
                            graphicsLayer.toImageBitmap().asAndroidBitmap()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // Snackbar for error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }

    // Photo Picker Dialog
    PhotoPickerDialog(
        isVisible = showPhotoPicker,
        onPhotoSelected = { uri ->
            viewModel.updatePicture(uri)
            showPhotoPicker = false
        },
        onDismiss = { showPhotoPicker = false },
        onError = { error -> viewModel.setError(error) }
    )
}

@Composable
private fun BirthdayContent(
    uiState: BirthdayUiState,
    onShowPhotoPicker: () -> Unit,
    onShareClick: () -> Unit
) {
    val birthday = uiState.birthdayInfo ?: return
    val age = AgeCalculator.getAge(birthday.dob)
    val ageNumberRes = age.number.getAgeNumberDrawable()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = BirthdayConst.Dimens.screenPaddingTop,
                    bottom = BirthdayConst.Dimens.screenPaddingBottom
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AgeContentSection(
                name = birthday.name,
                age = age,
                ageNumberRes = ageNumberRes
            )
            Spacer(modifier = Modifier.height(BirthdayConst.Dimens.spaceBetweenSections))

            PhotoSection(
                imageUri = uiState.selectedImageUri,
                theme = birthday.theme,
                onCameraClick = onShowPhotoPicker,
                modifier = Modifier
            )
        }

        Image(
            painter = painterResource(birthday.theme.getBackgroundImageResource()),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillHeight
        )

        Button(
            onClick = onShareClick,
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = shareBtnBg),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = BirthdayConst.Dimens.shareButtonBottomSpacing)
                .width(BirthdayConst.Dimens.shareButtonWidth)
                .height(BirthdayConst.Dimens.shareButtonHeight)
        ) {
            Text(
                text = stringResource(R.string.share_the_news),
                style = BirthdayConst.TextStyles.shareButtonText
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
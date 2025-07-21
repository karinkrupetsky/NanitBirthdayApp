package com.example.nanitbirthdayapp.ui.birthday.constants

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object BirthdayConst {
    object Dimens {
        val screenPaddingTop = 46.dp
        val screenPaddingBottom = 15.dp
        // Age Section
        val ageTitleHorizontalPadding = 72.dp
        val ageNumberSize = 104.dp
        val ageDecorationSize = 56.dp
        val ageNumberPadding = 16.dp
        val spaceBetweenAgeTitle = 13.dp
        val spaceBetweenAgeNumber = 14.dp

        // Image Section
        val imageSectionHorizontalPadding = 50.dp
        val babyImageSize = 224.dp
        val babyImageBorderWidth = 6.dp
        val cameraButtonSize = 36.dp

        val nanitLogoWidth = 59.dp
        val nanitLogoHeight = 20.dp
        val spaceBetweenSections = 15.dp
        val spaceBetweenPhotoAndLogo = 20.dp
        val shareButtonBottomSpacing = 32.dp
        val shareButtonWidth = 179.dp
        val shareButtonHeight = 42.dp
    }

    object TextStyles {
        val ageTitle = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Companion.Medium,
            fontFamily = FontFamily.Companion.SansSerif,
            lineHeight = 25.sp,
            color = Color(0xFF394562)
        )

        val ageUnit = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Companion.Medium,
            fontFamily = FontFamily.Companion.SansSerif,
            color = Color(0xFF394562)
        )

        val shareButtonText = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            lineHeight = 16.sp,
            letterSpacing = 0.sp,
            color = Color.White
        )
    }

    const val CONNECTION_SCREEN = "connection_screen"
    const val BIRTHDAY_SCREEN = "birthday_screen"
}
package com.example.nanitbirthdayapp.ui.birthday.constants

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object BirthdayConst {
    object Dimens {
        val screenPaddingTop = 22.dp
        val screenPaddingBottom = 15.dp
        val ageTitleHorizontalPadding = 72.dp
        val ageNumberSize = 104.dp
        val ageDecorationSize = 50.dp
        val ageNumberPadding = 16.dp
        val spaceBetweenAgeTitle = 13.dp
        val spaceBetweenAgeNumber = 14.dp
        val babyImageSize = 224.dp
        val nanitLogoWidth = 57.dp
        val nanitLogoHeight = 20.dp
        val spaceBetweenSections = 15.dp
        val spaceBetweenPhotoAndLogo = 15.dp
        val cameraButtonSize = 36.dp
    }

    object TextStyles {
        val ageTitle = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Companion.Medium,
            fontFamily = FontFamily.Companion.SansSerif,
            lineHeight = 21.sp,
            color = Color(0xFF394562)
        )

        val ageUnit = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Companion.Medium,
            fontFamily = FontFamily.Companion.SansSerif,
            color = Color(0xFF394562)
        )

        const val CONNECTION_SCREEN = "connection_screen"
        const val BIRTHDAY_SCREEN = "birthday_screen"
    }
}
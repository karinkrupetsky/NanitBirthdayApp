package com.example.nanitbirthdayapp.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.nanitbirthdayapp.R

fun BirthdayTheme.getThemeBackgroundColor(): Color = when (this) {
    BirthdayTheme.GREEN -> BirthdayGreen
    BirthdayTheme.YELLOW -> BirthdayYellow
    BirthdayTheme.BLUE -> BirthdayBlue
}

@DrawableRes
fun BirthdayTheme.getBackgroundImageResource(): Int = when (this) {
    BirthdayTheme.GREEN -> R.drawable.background_fox
    BirthdayTheme.YELLOW -> R.drawable.background_elephant
    BirthdayTheme.BLUE -> R.drawable.background_pelican
}

@DrawableRes
fun BirthdayTheme.getDefaultBabyImageResource(): Int = when (this) {
    BirthdayTheme.GREEN -> R.drawable.baby_placeholder_green
    BirthdayTheme.YELLOW -> R.drawable.baby_placeholder_yellow
    BirthdayTheme.BLUE -> R.drawable.baby_placeholder_blue
}

fun BirthdayTheme.getBabyImageBorderColor(): Color = when (this) {
    BirthdayTheme.GREEN -> BirthdayPlaceholderBorderGreen
    BirthdayTheme.YELLOW -> BirthdayPlaceholderBorderYellow
    BirthdayTheme.BLUE -> BirthdayPlaceholderBorderBlue
}

@DrawableRes
fun BirthdayTheme.getCameraIconResource(): Int = when (this) {
    BirthdayTheme.GREEN -> R.drawable.add_picture_green
    BirthdayTheme.YELLOW -> R.drawable.add_picture_yellow
    BirthdayTheme.BLUE -> R.drawable.add_picture_blue
}

@DrawableRes
fun Int.getAgeNumberDrawable(): Int = when (this) {
    1 -> R.drawable.number_1
    2 -> R.drawable.number_2
    3 -> R.drawable.number_3
    4 -> R.drawable.number_4
    5 -> R.drawable.number_5
    6 -> R.drawable.number_6
    7 -> R.drawable.number_7
    8 -> R.drawable.number_8
    9 -> R.drawable.number_9
    10 -> R.drawable.number_10
    11 -> R.drawable.number_11
    12 -> R.drawable.number_12
    else -> R.drawable.number_0
}

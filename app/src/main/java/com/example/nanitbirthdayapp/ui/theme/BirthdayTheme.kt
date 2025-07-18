package com.example.nanitbirthdayapp.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.nanitbirthdayapp.R

enum class BirthdayTheme(
    val backgroundColor: Color,
    val borderColor: Color,
    @DrawableRes val overlayImageRes: Int,
    @DrawableRes val defaultBabyImageRes: Int,
    @DrawableRes val addPhotoIconRes: Int
) {
    PELICAN(
        backgroundColor = PelicanBackground,
        borderColor = PelicanBlueBorder,
        overlayImageRes = R.drawable.bg_pelican,
        defaultBabyImageRes = R.drawable.default_baby_image_pelican,
        addPhotoIconRes = R.drawable.ic_add_a_photo_pelican
    ),
    FOX(
        backgroundColor = FoxBackground,
        borderColor = FoxGreenBorder,
        overlayImageRes = R.drawable.bg_fox,
        defaultBabyImageRes = R.drawable.default_baby_image_fox,
        addPhotoIconRes = R.drawable.ic_add_a_photo_fox
    ),
    ELEPHANT(
        backgroundColor = ElephantBackground,
        borderColor = ElephantYellowBorder,
        overlayImageRes = R.drawable.bg_elephant,
        defaultBabyImageRes = R.drawable.default_baby_image_elephant,
        addPhotoIconRes = R.drawable.ic_add_a_photo_elephant
    );


    companion object {
        fun fromString(themeName: String): BirthdayTheme {
            return when (themeName.lowercase()) {
                "pelican" -> PELICAN
                "fox" -> FOX
                "elephant" -> ELEPHANT
                else -> PELICAN // Default theme
            }
        }
    }
}

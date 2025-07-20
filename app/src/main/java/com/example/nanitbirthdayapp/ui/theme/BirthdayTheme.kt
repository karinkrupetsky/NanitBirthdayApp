package com.example.nanitbirthdayapp.ui.theme

enum class BirthdayTheme {
    GREEN,    // FOX
    YELLOW,   // ELEPHANT
    BLUE;     // PELICAN

    companion object {
        fun fromString(themeName: String): BirthdayTheme {
            return when (themeName.lowercase()) {
                "fox" -> GREEN
                "elephant" -> YELLOW
                "pelican" -> BLUE
                else -> BLUE
            }
        }

        fun random(): BirthdayTheme {
            return entries.random()
        }
    }
}
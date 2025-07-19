package com.example.nanitbirthdayapp.ui.navigation

import com.example.nanitbirthdayapp.ui.birthday.constants.BirthdayConst.TextStyles.BIRTHDAY_SCREEN
import com.example.nanitbirthdayapp.ui.birthday.constants.BirthdayConst.TextStyles.CONNECTION_SCREEN

sealed class Screen(val route: String) {
    object ConnectionScreen : Screen(CONNECTION_SCREEN)
    object BirthdayScreen : Screen(BIRTHDAY_SCREEN)
}

package com.example.nanitbirthdayapp.ui.navigation

import com.example.nanitbirthdayapp.ui.birthday.constants.BirthdayConst.BIRTHDAY_SCREEN
import com.example.nanitbirthdayapp.ui.birthday.constants.BirthdayConst.CONNECTION_SCREEN

sealed class Screen(val route: String) {
    object ConnectionScreen : Screen(CONNECTION_SCREEN)
    object BirthdayScreen : Screen(BIRTHDAY_SCREEN)
}

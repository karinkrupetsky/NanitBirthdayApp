package com.example.nanitbirthdayapp.domain.model

import com.example.nanitbirthdayapp.ui.theme.BirthdayTheme

data class Birthday(
    val name: String,
    val dob: Long,
    val theme: BirthdayTheme
)
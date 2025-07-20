package com.example.nanitbirthdayapp.data.mapper

import com.example.nanitbirthdayapp.data.model.BirthdayInfo
import com.example.nanitbirthdayapp.domain.model.Birthday
import com.example.nanitbirthdayapp.ui.theme.BirthdayTheme

fun BirthdayInfo.toDomain(): Birthday {
    return Birthday(
        name = this.name,
        dob = this.dob,
        theme = BirthdayTheme.fromString(this.theme)
    )
}
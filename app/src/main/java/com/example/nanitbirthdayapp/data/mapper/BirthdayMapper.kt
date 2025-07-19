package com.example.nanitbirthdayapp.data.mapper

import com.example.nanitbirthdayapp.data.model.BirthdayInfo
import com.example.nanitbirthdayapp.domain.model.Birthday

fun BirthdayInfo.toDomain(): Birthday {
    return Birthday(
        name = this.name,
        dob = this.dob,
        theme = this.theme
    )
}
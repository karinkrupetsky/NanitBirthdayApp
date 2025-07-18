package com.example.nanitbirthdayapp.util

data class Age(
    val number: Int,
    val unit: AgeUnit
)

enum class AgeUnit {
    MONTH, YEAR
}

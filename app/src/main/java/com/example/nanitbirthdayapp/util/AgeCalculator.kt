package com.example.nanitbirthdayapp.util

import java.util.*

object AgeCalculator {
    fun getAge(dobTimestamp: Long): Age {
        val dob = Calendar.getInstance().apply { timeInMillis = dobTimestamp }
        val today = Calendar.getInstance()

        val years = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        val months = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH)
        val days = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH)

        var totalMonths = years * 12 + months
        if (days < 0) {
            totalMonths--
        }

        return if (totalMonths < 12) {
            Age(maxOf(0, totalMonths), AgeUnit.MONTH)
        } else {
            val ageInYears = minOf(9, totalMonths / 12) // Max age 9 per requirements
            Age(ageInYears, AgeUnit.YEAR)
        }
    }
}

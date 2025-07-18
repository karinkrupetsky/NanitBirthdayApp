package com.example.nanitbirthdayapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.*

object AgeCalculator {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAge(dobTimestamp: Long): Age {
        val dob = LocalDate.ofInstant(Date(dobTimestamp).toInstant(), ZoneId.systemDefault())
        val today = LocalDate.now()
        val period = Period.between(dob, today)

        return if (period.years == 0) {
            Age(period.months, AgeUnit.MONTH)
        } else {
            Age(period.years, AgeUnit.YEAR)
        }
    }
}

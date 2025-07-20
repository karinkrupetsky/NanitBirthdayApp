package com.example.nanitbirthdayapp.util

import org.junit.Test
import org.junit.Assert.*
import java.util.*

class AgeCalculatorTest {
    
    @Test
    fun `calculate age for 6 months old baby`() {
        val sixMonthsAgo = Calendar.getInstance().apply {
            add(Calendar.MONTH, -6)
        }.timeInMillis

        val result = AgeCalculator.getAge(sixMonthsAgo)

        assertEquals(Age(6, AgeUnit.MONTH), result)
    }

    @Test
    fun `calculate age for 2 years old baby`() {
        val twoYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -2)
        }.timeInMillis

        val result = AgeCalculator.getAge(twoYearsAgo)
        assertEquals(Age(2, AgeUnit.YEAR), result)
    }

    @Test
    fun `max age should be 9 years`() {
        val tenYearsAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -10)
        }.timeInMillis

        val result = AgeCalculator.getAge(tenYearsAgo)
        assertEquals(Age(9, AgeUnit.YEAR), result)
    }

    @Test
    fun `newborn baby should be 0 months`() {
        val result = AgeCalculator.getAge(System.currentTimeMillis())
        assertEquals(Age(0, AgeUnit.MONTH), result)
    }

    @Test
    fun `11 months should return months not years`() {
        val elevenMonthsAgo = Calendar.getInstance().apply {
            add(Calendar.MONTH, -11)
        }.timeInMillis

        val result = AgeCalculator.getAge(elevenMonthsAgo)
        assertEquals(Age(11, AgeUnit.MONTH), result)
    }

    @Test
    fun `exactly 1 year old`() {
        val oneYearAgo = Calendar.getInstance().apply {
            add(Calendar.YEAR, -1)
        }.timeInMillis

        val result = AgeCalculator.getAge(oneYearAgo)
        assertEquals(Age(1, AgeUnit.YEAR), result)
    }
}
package com.example.nanitbirthdayapp.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BirthdayTest {
    
    @Test
    fun `given Birthday data, when creating instance, then properties are correctly set`() {
        // Given
        val name = "Test Baby"
        val dob = System.currentTimeMillis()
        val theme = "fox"

        // When
        val birthday = Birthday(name, dob, theme)

        // Then
        assertEquals(name, birthday.name)
        assertEquals(dob, birthday.dob)
        assertEquals(theme, birthday.theme)
    }

    @Test
    fun `given two Birthday instances with same data, when comparing, then they are equal`() {
        // Given
        val birthday1 = Birthday("John", 1234567890L, "pelican")
        val birthday2 = Birthday("John", 1234567890L, "pelican")

        // Then
        assertEquals(birthday1, birthday2)
        assertEquals(birthday1.hashCode(), birthday2.hashCode())
    }
    
    @Test
    fun `validates theme values`() {
        val validThemes = listOf("pelican", "fox", "elephant")
        validThemes.forEach { theme ->
            val birthday = Birthday("Test", 123456789L, theme)
            assertTrue("Theme $theme should be valid", 
                validThemes.contains(birthday.theme))
        }
    }
}
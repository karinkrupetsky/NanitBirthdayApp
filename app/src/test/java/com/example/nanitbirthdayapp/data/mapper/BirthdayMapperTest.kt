package com.example.nanitbirthdayapp.data.mapper

import com.example.nanitbirthdayapp.data.model.BirthdayInfo
import com.example.nanitbirthdayapp.domain.model.Birthday
import org.junit.Test
import org.junit.Assert.*

class BirthdayMapperTest {
    
    @Test
    fun `maps BirthdayInfo to Birthday correctly`() {
        val info = BirthdayInfo("John Doe", 1234567890L, "pelican")
        val result = info.toDomain()
        val expected = Birthday("John Doe", 1234567890L, "pelican")
        
        assertEquals(expected, result)
    }

    @Test
    fun `preserves theme in mapping`() {
        val themes = listOf("pelican", "fox", "elephant")
        
        themes.forEach { theme ->
            val info = BirthdayInfo("Test", System.currentTimeMillis(), theme)
            val result = info.toDomain()
            assertEquals(theme, result.theme)
        }
    }
}

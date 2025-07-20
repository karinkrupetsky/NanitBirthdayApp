package com.example.nanitbirthdayapp.core

import org.junit.Test
import org.junit.Assert.*

class ResourceTest {
    
    @Test
    fun `Resource Success contains data`() {
        val resource = Resource.Success("test data")
        assertEquals("test data", resource.data)
        assertNull(resource.message)
    }

    @Test
    fun `Resource Error contains message`() {
        val resource = Resource.Error<String>("error")
        assertEquals("error", resource.message)
        assertNull(resource.data)
    }

    @Test
    fun `Resource Loading is empty`() {
        val resource = Resource.Loading<String>()
        assertNull(resource.data)
        assertNull(resource.message)
    }
}
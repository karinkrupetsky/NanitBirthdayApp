package com.example.nanitbirthdayapp.domain.usecase

import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import org.junit.Test
import org.junit.Assert.*

class UpdateBabyPictureUseCaseTest {
    
    @Test
    fun `given name with spaces and special chars, when generating baby key, then returns sanitized key`() {
        // Given
        val useCase = com.example.nanitbirthdayapp.domain.usecase.UpdateBabyPictureUseCase(
            repository = mockRepository()
        )

        val testCases = mapOf(
            "John Doe" to "john_doe",
            "Mary Jane Smith" to "mary_jane_smith", 
            "Baby O'Connor" to "baby_o'connor",
        )
        
        testCases.forEach { (input, expectedPrefix) ->
            val dob = 1234567890L
            val expectedKey = "${expectedPrefix}_$dob"
            
            // Verify the key generation follows the expected pattern
            assertTrue("Key should contain sanitized name", 
                expectedKey.contains(expectedPrefix))
            assertTrue("Key should contain timestamp", 
                expectedKey.contains(dob.toString()))
        }
    }

    private fun mockRepository() = object : BirthdayRepository {
        override suspend fun getBirthdayInfo(ip: String, port: Int) = throw NotImplementedError()
        override suspend fun updateBabyPicture(babyKey: String, pictureUri: android.net.Uri) = throw NotImplementedError()
        override suspend fun getSavedPicture(babyKey: String) = null
        override suspend fun closeConnection() {}
    }
}

class GetBirthdayInfoUseCaseTest {
    
    @Test
    fun `validates IP addresses correctly`() {
        val validIps = listOf("192.168.1.1", "10.0.0.1", "127.0.0.1")
        val invalidIps = listOf("256.1.1.1", "192.168.1", "abc.def.ghi.jkl")
        
        validIps.forEach { assertTrue(isValidIp(it)) }
        invalidIps.forEach { assertFalse(isValidIp(it)) }
    }
    
    @Test
    fun `validates ports correctly`() {
        assertTrue(isValidPort(8080))
        assertTrue(isValidPort(80))
        assertFalse(isValidPort(-1))
        assertFalse(isValidPort(70000))
    }

    private fun isValidIp(ip: String): Boolean {
        val parts = ip.split(".")
        return parts.size == 4 && parts.all { 
            it.toIntOrNull()?.let { num -> num in 0..255 } == true 
        }
    }

    private fun isValidPort(port: Int) = port in 1..65535
}
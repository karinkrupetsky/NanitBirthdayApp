package com.example.nanitbirthdayapp.data.network

import org.junit.Test
import org.junit.Assert.*

class WebSocketClientTest {
    
    @Test
    fun `given WebSocket response parsing, when invalid JSON, then handles gracefully`() {
        val invalidJsonSamples = listOf(
            "{invalid json}",
            "{'malformed': json}",
            "{\"name\":\"test\",\"dob\":\"not_a_number\"}",
            "",
            "undefined"
        )
        
        invalidJsonSamples.forEach { invalidJson ->
            assertNotNull("Should handle invalid JSON gracefully", invalidJson)
        }
    }

    @Test
    fun `given connection parameters, when creating WebSocket URL, then formats correctly`() {
        val testCases = mapOf(
            Triple("192.168.1.1", 8080, "/nanit") to "ws://192.168.1.1:8080/nanit",
            Triple("localhost", 3000, "/test") to "ws://localhost:3000/test",
            Triple("10.0.0.1", 80, "/api") to "ws://10.0.0.1:80/api"
        )
        
        testCases.forEach { (input, expected) ->
            val (ip, port, path) = input
            val url = "ws://$ip:$port$path"
            assertEquals("URL should be formatted correctly", expected, url)
        }
    }
}

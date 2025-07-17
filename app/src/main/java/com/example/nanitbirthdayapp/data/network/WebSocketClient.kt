package com.example.nanitbirthdayapp.data.network

import android.util.Log
import com.example.nanitbirthdayapp.data.model.BirthdayInfo
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WebSocketClient @Inject constructor() {
    private val client = HttpClient(CIO) {
        install(WebSockets)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    fun connect(ip: String, port: Int): Flow<Result<BirthdayInfo>> = flow {
        try {
            client.webSocket(
                method = HttpMethod.Get,
                host = ip,
                port = port,
                path = "/nanit"
            ) {
                send(Frame.Text("HappyBirthday"))
                val frame = incoming.receive()
                if (frame is Frame.Text) {
                    val jsonText = frame.readText()
                    Log.d("WebSocketClient", "Received JSON: $jsonText")
                    if (jsonText != "null") {
                        val birthdayInfo = Json.decodeFromString<BirthdayInfo>(jsonText)
                        emit(Result.success(birthdayInfo))
                    } else {
                        emit(Result.failure(Exception("Server returned null. Please enter data in the server app.")))
                    }
                }
                close(CloseReason(CloseReason.Codes.NORMAL, "Client closed connection"))
            }
        } catch (e: Exception) {
            Log.e("WebSocketClient", "WebSocket connection error", e)
            emit(Result.failure(e))
        }
    }

    fun close() {
        client.close()
    }
}

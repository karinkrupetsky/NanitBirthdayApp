package com.example.nanitbirthdayapp.data.network

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class WebSocketClient @Inject constructor() {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    fun connect(ip: String, port: Int, path: String = "/nanit"): Flow<Result<String>> = flow {
        try {
            Log.d("WebSocketClient", "Attempting to connect to ws://$ip:$port$path")

            client.webSocket(
                method = HttpMethod.Get,
                host = ip,
                port = port,
                path = path
            ) {
                Log.d("WebSocketClient", "WebSocket connection established")
                send(Frame.Text("HappyBirthday"))
                val frame = withTimeoutOrNull(5000) {
                    incoming.receive()
                }

                if (frame == null) {
                    emit(Result.failure(Exception("No response from server (timeout)")))
                } else if (frame is Frame.Text) {
                    val response = frame.readText()
                    Log.d("WebSocketClient", "Received response: $response")
                    emit(Result.success(response))
                } else {
                    emit(Result.failure(Exception("Invalid response format")))
                }

                close(CloseReason(CloseReason.Codes.NORMAL, "Client closed connection"))
            }
        } catch (e: Exception) {
            Log.e("WebSocketClient", "Connection error: ${e.message}", e)
            emit(Result.failure(e))
        }
    }

    fun close() {
        client.close()
    }
}

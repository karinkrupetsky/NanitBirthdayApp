package com.example.nanitbirthdayapp.data.repository

import com.example.nanitbirthdayapp.data.model.BirthdayInfo
import com.example.nanitbirthdayapp.data.network.WebSocketClient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BirthdayRepository @Inject constructor(
    private val webSocketClient: WebSocketClient
) {
    fun getBirthdayInfo(ip: String, port: Int): Flow<Result<BirthdayInfo>> {
        return webSocketClient.connect(ip, port)
    }

    suspend fun closeConnection() {
        webSocketClient.close()
    }
}

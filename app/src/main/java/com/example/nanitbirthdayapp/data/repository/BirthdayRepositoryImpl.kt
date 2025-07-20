package com.example.nanitbirthdayapp.data.repository

import android.content.Context
import android.net.Uri
import com.example.nanitbirthdayapp.core.Resource
import com.example.nanitbirthdayapp.data.local.SharedPreferencesManager
import com.example.nanitbirthdayapp.data.mapper.toDomain
import com.example.nanitbirthdayapp.data.model.BirthdayInfo
import com.example.nanitbirthdayapp.data.network.WebSocketClient
import com.example.nanitbirthdayapp.domain.model.Birthday
import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class BirthdayRepositoryImpl @Inject constructor(
    private val webSocketClient: WebSocketClient,
    @ApplicationContext private val context: Context,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BirthdayRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getBirthdayInfo(ip: String, port: Int): Flow<Result<Birthday>> {
        return webSocketClient.connect(ip, port).map { result ->
            result.mapCatching { jsonResponse ->
                parseResponse(jsonResponse).toDomain()
            }
        }
    }

    private fun parseResponse(jsonText: String): BirthdayInfo {
        return when {
            jsonText == "null" -> {
                throw Exception("No data available. Please enter name and DOB in the server app.")
            }
            jsonText.isBlank() -> {
                throw Exception("Empty response from server")
            }
            else -> {
                try {
                    json.decodeFromString<BirthdayInfo>(jsonText)
                } catch (e: Exception) {
                    throw Exception("Invalid data format from server")
                }
            }
        }
    }

    override suspend fun updateBabyPicture(babyKey: String, pictureUri: Uri): Flow<Resource<Uri>> = flow {
        emit(Resource.Loading())

        try {
            val savedUri = withContext(Dispatchers.IO) {
                val inputStream = context.contentResolver.openInputStream(pictureUri)
                    ?: return@withContext null

                val internalDir = File(context.filesDir, "baby_images")
                if (!internalDir.exists()) {
                    internalDir.mkdirs()
                }
                val destinationFile = File(internalDir, "$babyKey.jpg")
                if (destinationFile.exists()) {
                    destinationFile.delete()
                }

                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                    outputStream.flush()
                }

                inputStream.close()

                // Save path to preferences for this specific baby
                sharedPreferencesManager.saveImagePathForBaby(babyKey, destinationFile.absolutePath)

                Uri.fromFile(destinationFile)
            }

            if (savedUri != null) {
                emit(Resource.Success(savedUri))
            } else {
                emit(Resource.Error("Failed to save image"))
            }

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override suspend fun getSavedPicture(babyKey: String): Uri? {
        val savedPath = sharedPreferencesManager.getImagePathForBaby(babyKey)
        return savedPath?.let { path ->
            val file = File(path)
            if (file.exists()) {
                Uri.fromFile(file)
            } else null
        }
    }

    override suspend fun closeConnection() {
        webSocketClient.close()
    }
}
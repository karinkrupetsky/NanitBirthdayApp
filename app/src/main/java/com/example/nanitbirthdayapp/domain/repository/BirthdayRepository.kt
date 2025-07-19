package com.example.nanitbirthdayapp.domain.repository

import android.net.Uri
import com.example.nanitbirthdayapp.core.Resource
import com.example.nanitbirthdayapp.domain.model.Birthday
import kotlinx.coroutines.flow.Flow

interface BirthdayRepository {
    suspend fun getBirthdayInfo(ip: String, port: Int): Flow<Result<Birthday>>
    suspend fun updateBabyPicture(babyKey: String, pictureUri: Uri): Flow<Resource<Uri>>
    suspend fun getSavedPicture(babyKey: String): Uri?
    suspend fun closeConnection()
}
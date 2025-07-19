package com.example.nanitbirthdayapp.domain.usecase

import android.net.Uri
import com.example.nanitbirthdayapp.core.Resource
import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateBabyPictureUseCase @Inject constructor(
    private val repository: BirthdayRepository
) {
    suspend operator fun invoke(babyKey: String, pictureUri: Uri): Flow<Resource<Uri>> {
        return repository.updateBabyPicture(babyKey, pictureUri)
    }
}
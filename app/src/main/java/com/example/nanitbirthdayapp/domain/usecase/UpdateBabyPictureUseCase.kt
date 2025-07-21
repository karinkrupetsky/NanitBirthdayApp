package com.example.nanitbirthdayapp.domain.usecase

import android.net.Uri
import com.example.nanitbirthdayapp.core.Resource
import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import com.example.nanitbirthdayapp.domain.util.BabyKeyGenerator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateBabyPictureUseCase @Inject constructor(
    private val repository: BirthdayRepository
) {
    suspend operator fun invoke(name: String, dob: Long, pictureUri: Uri): Flow<Resource<Uri>> {
        val babyKey = BabyKeyGenerator.generate(name, dob)
        return repository.updateBabyPicture(babyKey, pictureUri)
    }
}
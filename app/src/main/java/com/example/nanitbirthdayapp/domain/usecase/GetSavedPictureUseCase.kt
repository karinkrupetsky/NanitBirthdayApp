package com.example.nanitbirthdayapp.domain.usecase

import android.net.Uri
import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import com.example.nanitbirthdayapp.domain.util.BabyKeyGenerator
import javax.inject.Inject

class GetSavedPictureUseCase @Inject constructor(
    private val repository: BirthdayRepository
) {
    suspend operator fun invoke(name: String, dob: Long): Uri? {
        val babyKey = BabyKeyGenerator.generate(name, dob)
        return repository.getSavedPicture(babyKey)
    }
}

package com.example.nanitbirthdayapp.domain.usecase

import android.net.Uri
import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import javax.inject.Inject

class GetSavedPictureUseCase @Inject constructor(
    private val repository: BirthdayRepository
) {
    suspend operator fun invoke(name: String, dob: Long): Uri? {
        val babyKey = generateBabyKey(name, dob)
        return repository.getSavedPicture(babyKey)
    }

    private fun generateBabyKey(name: String, dob: Long): String {
        return "${name.replace(" ", "_").lowercase()}_$dob"
    }
}

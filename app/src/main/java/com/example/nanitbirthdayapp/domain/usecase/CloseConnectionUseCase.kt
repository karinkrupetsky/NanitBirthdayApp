package com.example.nanitbirthdayapp.domain.usecase

import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import javax.inject.Inject

class CloseConnectionUseCase @Inject constructor(
    private val repository: BirthdayRepository
) {
    suspend operator fun invoke() {
        repository.closeConnection()
    }
}
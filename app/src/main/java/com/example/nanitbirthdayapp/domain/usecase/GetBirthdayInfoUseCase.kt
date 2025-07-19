package com.example.nanitbirthdayapp.domain.usecase

import com.example.nanitbirthdayapp.domain.model.Birthday
import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBirthdayInfoUseCase @Inject constructor(
    private val repository: BirthdayRepository
) {
    suspend operator fun invoke(ip: String, port: Int): Flow<Result<Birthday>> {
        return repository.getBirthdayInfo(ip, port)
    }
}
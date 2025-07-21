package com.example.nanitbirthdayapp.domain.usecase

import com.example.nanitbirthdayapp.domain.exception.InvalidConnectionParametersException
import com.example.nanitbirthdayapp.domain.model.Birthday
import com.example.nanitbirthdayapp.domain.repository.BirthdayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBirthdayInfoUseCase @Inject constructor(
    private val repository: BirthdayRepository
) {
    suspend operator fun invoke(ip: String, port: Int = 8080): Flow<Result<Birthday>> {
        return if (isValidIpAddress(ip) && isValidPort(port)) {
            repository.getBirthdayInfo(ip, port)
        } else {
            flow {
                emit(Result.failure(InvalidConnectionParametersException()))
            }
        }
    }

    private fun isValidIpAddress(ip: String): Boolean {
        val parts = ip.split(".")
        if (parts.size != 4) return false
        return parts.all { part ->
            try {
                val num = part.toInt()
                num in 0..255
            } catch (e: NumberFormatException) {
                false
            }
        }
    }

    private fun isValidPort(port: Int): Boolean {
        return port in 1..65535
    }
}
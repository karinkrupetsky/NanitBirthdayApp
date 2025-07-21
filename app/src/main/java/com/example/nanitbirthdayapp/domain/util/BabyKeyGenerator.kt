package com.example.nanitbirthdayapp.domain.util

object BabyKeyGenerator {
    fun generate(name: String, dob: Long): String {
        return "${name.replace(" ", "_").lowercase()}_$dob"
    }
}

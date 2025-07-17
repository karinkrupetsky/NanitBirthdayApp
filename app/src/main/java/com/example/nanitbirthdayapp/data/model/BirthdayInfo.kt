package com.example.nanitbirthdayapp.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@OptIn(InternalSerializationApi::class)
data class BirthdayInfo(
    val name: String,
    val dob: Long,
    val theme: String
)
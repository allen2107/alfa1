package ru.alfa.dto

data class AtmDto (
        val deviceId: Long,
        val latitude: String?,
        val longitude: String?,
        val city: String,
        val location: String,
        val payments: Boolean
)

package ru.alfa.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SourceAtmDto(
    val deviceId: Long,
    val address: Map<String, String>,
    val coordinates: Map<String, String>,
    val services: Map<String, String>
)

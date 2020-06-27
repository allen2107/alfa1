package ru.alfa.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetAtmsResponse (
        val data: AtmDataDto
)

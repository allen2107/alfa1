package ru.alfa.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AtmDataDto (
        val atms: List<SourceAtmDto>
)

package com.example.townservice.dtos

import com.example.townservice.configuration.annotation.DoubleRange
import jakarta.validation.constraints.NotBlank
import java.util.*

data class WeatherDto(
    val id: UUID?,
    @field:NotBlank
    @field:DoubleRange(min = -50.0, max = 50.0)
    val temperature: Double,
)
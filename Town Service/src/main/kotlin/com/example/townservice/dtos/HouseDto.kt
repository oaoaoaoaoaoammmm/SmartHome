package com.example.townservice.dtos

import com.example.townservice.configuration.annotation.DoubleRange
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class HouseDto(
    val id: UUID?,
    @field:NotBlank
    val address: String,
    @field:NotNull
    @field:DoubleRange(min = 0.5, max = 1.0)
    val wallThickness: Double,
)

package com.example.townservice.dtos

import com.example.townservice.configuration.annotation.DoubleRange
import com.example.townservice.models.enumerations.CommunalType
import jakarta.validation.constraints.NotNull
import java.util.*

data class TariffDto(
    val id: UUID?,
    @field:NotNull
    var communalType: CommunalType,
    @field:NotNull
    @field:DoubleRange(min = 0.0, max = Double.MAX_VALUE)
    val cost: Double
)

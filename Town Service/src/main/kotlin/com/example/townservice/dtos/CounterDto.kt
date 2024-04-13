package com.example.townservice.dtos

import com.example.townservice.models.enumerations.CommunalType
import jakarta.validation.constraints.NotNull
import java.util.*

data class CounterDto(
    val id: UUID?,
    @field:NotNull
    val value: Double,
    @field:NotNull
    val communalType: CommunalType
)

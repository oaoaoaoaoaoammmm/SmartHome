package com.example.townservice.dtos

import jakarta.validation.constraints.NotBlank
import java.util.*

data class TownDto(
    val id: UUID?,
    @field:NotBlank
    val name: String
)

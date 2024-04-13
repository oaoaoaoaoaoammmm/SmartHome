package com.example.townservice.dtos

import com.example.townservice.models.enumerations.CommunalType
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*

data class ReceiptDto(
    val id: UUID?,
    @field:NotNull
    var payment: Boolean,
    @field:NotNull
    var communalType: CommunalType,
    @field:NotNull
    var debt: Double,
    @field:NotNull
    var previousCounterValue: Double,
    @field:NotNull
    var currentCounterValue: Double,
    @field:NotNull
    var date: LocalDateTime
)
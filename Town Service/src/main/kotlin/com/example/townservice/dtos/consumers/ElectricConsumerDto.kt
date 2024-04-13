package com.example.townservice.dtos.consumers

import com.example.townservice.configuration.annotation.DoubleRange
import com.example.townservice.models.enumerations.ElectricConsumerType
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*

data class ElectricConsumerDto(
    val id: UUID?,
    @field:NotNull
    @field:DoubleRange(min = 0.0, max = 100.0)
    val electricPower: Double,
    val timeLastSwitchOn: LocalDateTime,
    val timeLastSwitchOff: LocalDateTime,
    @field:NotNull
    val electricConsumerType: ElectricConsumerType
)
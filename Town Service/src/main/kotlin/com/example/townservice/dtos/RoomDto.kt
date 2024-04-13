package com.example.townservice.dtos

import com.example.townservice.dtos.consumers.ElectricConsumerDto
import jakarta.validation.constraints.NotNull
import java.util.*

data class RoomDto(
    val id: UUID?,
    @field:NotNull
    val electricConsumersDto: List<ElectricConsumerDto>,
)
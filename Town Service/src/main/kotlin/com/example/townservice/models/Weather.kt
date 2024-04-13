package com.example.townservice.models

import com.example.townservice.configuration.annotation.DoubleRange
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "weather")
class Weather(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    @field:DoubleRange(min = -50.0, max = 50.0)
    var temperature: Double,
    @field:OneToOne(fetch = FetchType.LAZY)
    var town: Town?
)
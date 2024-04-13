package com.example.townservice.models

import com.example.townservice.configuration.annotation.Min
import com.example.townservice.models.enumerations.CommunalType
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "receipt")
class Receipt(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,

    @field:NotNull
    var payment: Boolean,

    @field:NotNull
    @field:Enumerated(EnumType.STRING)
    var communalType: CommunalType,

    @field:NotNull
    var debt: Double,

    @field:NotNull
    @field:Min(min = 0.0, message = "PreviousCounterValue must be >= 0")
    var previousCounterValue: Double,

    @field:NotNull
    @field:Min(min = 0.0, message = "CurrentCounterValue must be >= 0")
    var currentCounterValue: Double,

    @field:NotNull
    var date: LocalDateTime,

    @field:ManyToOne(fetch = FetchType.LAZY)
    var house: House?
)
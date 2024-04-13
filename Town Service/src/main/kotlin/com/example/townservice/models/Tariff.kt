package com.example.townservice.models

import com.example.townservice.configuration.annotation.Min
import com.example.townservice.models.enumerations.CommunalType
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.util.*

@Entity
@Table(name = "tariff")
class Tariff(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,

    @field:NotNull
    @field:Enumerated(EnumType.STRING)
    var communalType: CommunalType,

    @field:NotNull
    @field:Min(min = 0.0, message = "Must be value >= 0")
    var cost: Double,

    @field:NotNull
    @field:ManyToOne(fetch = FetchType.LAZY)
    var town: Town?
)

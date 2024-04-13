package com.example.townservice.models

import com.example.townservice.configuration.annotation.Min
import com.example.townservice.models.enumerations.CommunalType
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.util.*

@Entity
@Table(name = "counter")
class Counter(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,

    @field:NotNull
    @field:Min(min = 0.0, message = "Must be value >= 0")
    var value: Double,

    @field:NotNull
    @field:Enumerated(EnumType.STRING)
    var communalType: CommunalType,

    @field:ManyToOne(fetch = FetchType.LAZY)
    var house: House?
)
package com.example.townservice.models

import com.example.townservice.configuration.annotation.DoubleRange
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

@Entity
@Table(name = "house")
class House(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,

    @field:NotBlank
    var address: String,

    @field:NotNull
    @field:DoubleRange(min = 0.5, max = 1.0, message = "Must be 0.5 <= wallThickness <= 1.0")
    var wallThickness: Double,

    @field:ManyToOne(fetch = FetchType.LAZY)
    var town: Town?
)
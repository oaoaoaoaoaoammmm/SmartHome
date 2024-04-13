package com.example.townservice.models


import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.util.*

@Entity
@Table(name = "town")
class Town(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,

    @field:NotBlank
    var name: String
)


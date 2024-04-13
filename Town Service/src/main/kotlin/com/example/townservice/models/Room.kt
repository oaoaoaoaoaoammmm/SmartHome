package com.example.townservice.models

import com.example.townservice.models.consumers.ElectricConsumer
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "room")
class Room(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,

    @field:OneToMany(fetch = FetchType.EAGER)
    var electricConsumers: MutableList<ElectricConsumer>,

    @field:ManyToOne(fetch = FetchType.LAZY)
    var house: House?
)
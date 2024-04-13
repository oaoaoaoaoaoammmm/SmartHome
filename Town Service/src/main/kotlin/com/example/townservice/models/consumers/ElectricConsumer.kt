package com.example.townservice.models.consumers

import com.example.townservice.configuration.annotation.DoubleRange
import com.example.townservice.models.Counter
import com.example.townservice.models.enumerations.ElectricConsumerType
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*


@Entity
@Table(name = "electric_consumer")
class ElectricConsumer(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,

    var timeLastSwitchOn: LocalDateTime,

    var timeLastSwitchOff: LocalDateTime,

    @NotNull
    @DoubleRange(min = 0.0, max = 100.0, message = "Electric power must be [0.0; 100.0]")
    var electricPower: Double,

    @Enumerated(EnumType.STRING)
    var electricConsumerType: ElectricConsumerType,

    @field:ManyToOne(fetch = FetchType.LAZY)
    var counter: Counter?
)
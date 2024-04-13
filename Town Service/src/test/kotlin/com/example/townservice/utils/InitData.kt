package com.example.townservice.utils

import com.example.townservice.models.Tariff
import com.example.townservice.models.Town
import com.example.townservice.models.Weather
import com.example.townservice.models.enumerations.CommunalType
import java.util.*

val SPRINGFIELD_TOWN = Town(
    id = UUID.randomUUID(),
    name = "Springfield"
)

val SPRINGFIELD_WEATHER = Weather(
    id = UUID.randomUUID(),
    temperature = 20.0,
    town = SPRINGFIELD_TOWN
)

val SPRINGFIELD_ELECTRIC_TARIFF = Tariff(
    id = UUID.randomUUID(),
    communalType = CommunalType.ELECTRIC,
    cost = 10.0,
    town = SPRINGFIELD_TOWN
)
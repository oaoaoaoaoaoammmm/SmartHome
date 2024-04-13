package com.example.townservice.repositories

import com.example.townservice.models.Weather
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WeatherRepository : JpaRepository<Weather, UUID> {
    fun findWeatherByTownId(townId: UUID): Optional<Weather>


    @Procedure(procedureName = "calc_new_consumer_power_and_counter_value")
    fun callChangeConvertersTemperature(weatherTownId: UUID, newTemp: Double): Void
}
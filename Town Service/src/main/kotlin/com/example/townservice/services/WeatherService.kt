package com.example.townservice.services

import com.example.townservice.models.Weather
import com.example.townservice.repositories.WeatherRepository
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class WeatherService(
    private val weatherRepository: WeatherRepository
) {

    fun findWeatherByTown(townId: UUID): Weather {
        logger.info { "Find weather by town id - $townId" }
        return weatherRepository.findWeatherByTownId(townId)
            .orElseThrow { throw NoSuchElementException("Can't find weather") }
    }

    fun findWeatherById(weatherId: UUID): Weather {
        logger.info { "Find weather by id - $weatherId" }
        return weatherRepository.findById(weatherId)
            .orElseThrow { throw NoSuchElementException("Can't find weather") }
    }

    fun saveWeather(weather: Weather): Weather {
        logger.info { "Save weather" }
        return weatherRepository.save(weather)
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    fun updateWeatherTemperature(townId: UUID, weather: Weather): Weather {
        logger.info { "Update weather by town id - $townId weather - $weather" }
        val newWeather = findWeatherByTown(townId)

        with(newWeather) {
            temperature = weather.temperature
        }

        weatherRepository.callChangeConvertersTemperature(townId, weather.temperature)
        return newWeather
    }

    private companion object : KLogging()
}
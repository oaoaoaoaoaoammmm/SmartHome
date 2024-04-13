package com.example.townservice.services

import com.example.townservice.models.Tariff
import com.example.townservice.models.Town
import com.example.townservice.models.Weather
import com.example.townservice.models.enumerations.CommunalType
import com.example.townservice.repositories.TownRepository
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TownService(
    private val townRepository: TownRepository,
    private val tariffService: TariffService,
    private val weatherService: WeatherService
) {

    fun findAllTowns(): Collection<Town> {
        logger.info { "Find towns" }
        return townRepository.findAll()
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    fun addTown(town: Town): Town {

        logger.info { "Add town - $town" }

        townRepository.save(town)

        val weather = Weather(
            id = null,
            temperature = 20.0,
            town = town
        )
        weatherService.saveWeather(weather)

        val tariffs = CommunalType.entries
            .map { type ->
                Tariff(
                    id = null,
                    communalType = type,
                    cost = 10.0,
                    town = town
                )
            }
        tariffService.saveTariffs(tariffs)

        return town
    }

    fun deleteTown(townId: UUID) {
        logger.info { "Delete town by id - $townId" }
        townRepository.deleteById(townId)
    }

    fun findTown(townId: UUID): Town {
        logger.info { "Find town by id - $townId" }
        return townRepository.findById(townId)
            .orElseThrow { throw NoSuchElementException("Can't find town") }
    }

    private companion object : KLogging()
}
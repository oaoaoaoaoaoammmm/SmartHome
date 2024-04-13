package com.example.townservice.controller

import com.example.townservice.configuration.annotation.RestMappingController
import com.example.townservice.controller.TownController.Companion.ROOT_URI
import com.example.townservice.dtos.TariffDto
import com.example.townservice.dtos.TownDto
import com.example.townservice.dtos.WeatherDto
import com.example.townservice.mappers.TownMapper
import com.example.townservice.services.TariffService
import com.example.townservice.services.TownService
import com.example.townservice.services.WeatherService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotNull
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Validated
@Tag(name = "Town's controller")
@RestMappingController(ROOT_URI)
class TownController(
    private val townService: TownService,
    private val weatherService: WeatherService,
    private val tariffService: TariffService,
    private val townMapper: TownMapper,
) {

    @Operation(summary = "Find all towns")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAllTowns(): Collection<TownDto> {
        logger.trace { "Find towns" }
        val towns = townService.findAllTowns()
            .map(townMapper::toTownDto)
        logger.trace { "Found towns - $towns" }
        return towns
    }

    @Operation(summary = "Add a town")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addTown(
        @RequestBody @NotNull @Parameter(name = "townDto", required = true) townDto: TownDto
    ): TownDto {
        val town = townMapper.toTown(townDto)
        logger.trace { "Add a town - $town" }
        val dto = townService.addTown(town)
            .let(townMapper::toTownDto)
        logger.trace { "Added the town - $dto" }
        return dto
    }

    @Operation(summary = "Delete a town")
    @DeleteMapping("/{townId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTown(
        @PathVariable @NotNull townId: UUID
    ) {
        logger.trace { "Delete town by id - $townId" }
        townService.deleteTown(townId)
        logger.trace { "The town deleted id - $townId" }
    }

    @Operation(summary = "Find a town")
    @GetMapping("/{townId}")
    @ResponseStatus(HttpStatus.OK)
    fun findTown(
        @PathVariable @NotNull townId: UUID
    ): TownDto {
        logger.trace { "Find a town by id - $townId" }
        val town = townService.findTown(townId)
            .let(townMapper::toTownDto)
        logger.trace { "The town found - $town" }
        return town
    }

    @Operation(summary = "Find weather in a town")
    @GetMapping("/{townId}/$WEATHER")
    @ResponseStatus(HttpStatus.OK)
    fun findWeatherByTown(
        @PathVariable @NotNull townId: UUID
    ): WeatherDto {
        logger.trace { "Find weather by town id - $townId" }
        val weather = weatherService.findWeatherByTown(townId)
            .let(townMapper::toWeatherDto)
        logger.trace { "Found weather - $weather in town by id - $townId" }
        return weather
    }

    @Operation(summary = "Update weather by town")
    @PatchMapping("/{townId}/$WEATHER")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    fun updateWeather(
        @PathVariable @NotNull townId: UUID,
        @RequestBody @NotNull @Parameter(name = "weatherDto", required = true) weatherDto: WeatherDto
    ): WeatherDto {
        val weather = townMapper.toWeather(weatherDto)
        logger.trace { "Update weather by town id - $townId weather - $weather" }
        val dto = weatherService.updateWeatherTemperature(townId, weather)
            .let(townMapper::toWeatherDto)
        logger.trace { "The weather updated - $dto town id - $townId" }
        return dto
    }

    @Operation(summary = "Find all tariffs by town")
    @GetMapping("/{townId}/$TARIFFS")
    @ResponseStatus(HttpStatus.OK)
    fun findAllTariffs(
        @PathVariable @NotNull townId: UUID
    ): Collection<TariffDto> {
        logger.trace { "Find tariffs by town id - $townId" }
        val tariffs = tariffService.findTariffsByTown(townId)
            .map(townMapper::toTariffDto)
        logger.trace { "Found tariffs - $tariffs" }
        return tariffs
    }

    @Operation(summary = "Update a tariff by id")
    @PatchMapping("/{townId}/$TARIFFS/{tariffId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateTariff(
        @PathVariable townId: UUID,
        @PathVariable @NotNull tariffId: UUID,
        @RequestBody @NotNull @Parameter(name = "tariffDto", required = true) tariffDto: TariffDto,
    ): TariffDto {
        val tariff = townMapper.toTariff(tariffDto)
        logger.trace { "Update tariff by town id - $townId tariff $tariff" }
        val dto = tariffService.updateTariff(tariffId, tariff)
            .let(townMapper::toTariffDto)
        logger.trace { "The tariff updated $dto town id - $townId" }
        return dto
    }

    companion object : KLogging() {
        const val ROOT_URI = "towns"
        const val WEATHER = "weather"
        const val TARIFFS = "tariffs"
    }
}
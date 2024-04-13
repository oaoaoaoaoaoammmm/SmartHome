package com.example.townservice.controller

import com.example.townservice.configuration.annotation.RestMappingController
import com.example.townservice.controller.HouseController.Companion.ROOT_URI
import com.example.townservice.dtos.HouseDto
import com.example.townservice.mappers.HouseMapper
import com.example.townservice.services.HouseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import mu.KLogging
import org.springdoc.core.converters.models.PageableAsQueryParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Validated
@Tag(name = "House's controller")
@RestMappingController(ROOT_URI)
class HouseController(
    private val houseService: HouseService,
    private val houseMapper: HouseMapper
) {

    @Operation(summary = "Find house's page in a town")
    @GetMapping
    @PageableAsQueryParam
    fun findAllHousesInTown(
        @RequestParam @NotNull @Parameter(name = "townId", required = true) townId: UUID,
        @Schema(hidden = true) @SortDefault("id", direction = Sort.Direction.ASC) @PageableDefault pageable: Pageable
    ): Page<HouseDto> {
        logger.trace { "Find houses in a town by id - $townId" }
        val page = houseService.findPageHousesInTown(townId, pageable)
            .map(houseMapper::toHouseDto)
        logger.trace { "Found houses in a town - ${page.content}" }
        return page
    }

    @Operation(summary = "Add a house in a town")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addHouseInTown(
        @RequestParam @NotNull @Parameter(name = "townId", required = true) townId: UUID,
        @RequestParam @NotNull @Min(1) @Max(5) @Parameter(name = "roomCount", required = true) roomCount: Int,
        @RequestBody @NotNull @Parameter(name = "houseDto", required = true) houseDto: HouseDto
    ): HouseDto {
        val house = houseMapper.toHouse(houseDto)
        logger.trace { "Add a house - $house in a town by id - $townId" }
        val dto = houseService.addHouseInTown(townId, house, roomCount)
            .let(houseMapper::toHouseDto)
        logger.trace { "The House added - $dto" }
        return dto
    }

    @Operation(summary = "Delete a house")
    @DeleteMapping("/{houseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteHouse(
        @PathVariable @NotNull houseId: UUID
    ) {
        logger.trace { "Delete a house by id - $houseId" }
        houseService.deleteHouse(houseId)
        logger.trace { "The House deleted id - $houseId" }
    }

    @Operation(summary = "Find a house")
    @GetMapping("/{houseId}")
    @ResponseStatus(HttpStatus.OK)
    fun findHouse(
        @PathVariable @NotNull houseId: UUID
    ): HouseDto {
        logger.trace { "Find a house by id - $houseId" }
        val house = houseService.findHouse(houseId)
            .let(houseMapper::toHouseDto)
        logger.trace { "Found the house - $house" }
        return house
    }

    companion object : KLogging() {
        const val ROOT_URI = "/houses"
    }
}
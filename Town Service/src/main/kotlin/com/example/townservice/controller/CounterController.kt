package com.example.townservice.controller

import com.example.townservice.configuration.annotation.RestMappingController
import com.example.townservice.controller.CounterController.Companion.ROOT_URI
import com.example.townservice.dtos.CounterDto
import com.example.townservice.mappers.CounterMapper
import com.example.townservice.services.CounterService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotNull
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@Validated
@Tag(name = "Counter's controller")
@RestMappingController(ROOT_URI)
class CounterController(
    private val counterService: CounterService,
    private val counterMapper: CounterMapper
) {

    @Operation(summary = "Find all counters in a house")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAllCountersInHouse(
        @RequestParam @NotNull @Parameter(name = "houseId", required = true) houseId: UUID
    ): Collection<CounterDto> {
        logger.trace { "Find counters in house by id - $houseId" }
        val counters = counterService.findAllCountersInHouse(houseId)
            .map(counterMapper::toCounterDto)
        logger.trace { "Found counters in house - $counters" }
        return counters
    }

    companion object : KLogging() {
        const val ROOT_URI = "/counters"
    }
}
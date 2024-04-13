package com.example.townservice.controller

import com.example.townservice.configuration.annotation.RestMappingController
import com.example.townservice.controller.RoomController.Companion.ROOT_URI
import com.example.townservice.dtos.RoomDto
import com.example.townservice.dtos.consumers.ElectricConsumerDto
import com.example.townservice.mappers.RoomMapper
import com.example.townservice.services.ElectricConsumerService
import com.example.townservice.services.RoomService
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
@Tag(name = "Room's controller")
@RestMappingController(ROOT_URI)
class RoomController(
    private val roomService: RoomService,
    private val electricConsumerService: ElectricConsumerService,
    private val roomMapper: RoomMapper
) {

    @Operation(summary = "Find all rooms in a house")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAllRoomsInHouse(
        @RequestParam @NotNull @Parameter(name = "houseId", required = true) houseId: UUID
    ): Collection<RoomDto> {
        logger.trace { "Find rooms in a house by id - $houseId" }
        val rooms = roomService.findAllRoomsInHouse(houseId)
            .map(roomMapper::toRoomDto)
        logger.trace { "Found rooms - $rooms in house id - $houseId" }
        return rooms
    }

    @Operation(summary = "Find room")
    @GetMapping("/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    fun findRoom(
        @PathVariable @NotNull roomId: UUID
    ): RoomDto {
        logger.trace { "Find a room by id - $roomId" }
        val room = roomService.findRoom(roomId)
            .let(roomMapper::toRoomDto)
        logger.trace { "Found the room - $room" }
        return room
    }

    @Operation(summary = "Switch a electric consumer")
    @PatchMapping("$LIGHT_SWITCHERS/{elConsumerId}")
    @ResponseStatus(HttpStatus.OK)
    fun switchLightSwitcher(
        @PathVariable @NotNull elConsumerId: UUID,
        @NotNull @Parameter(name = "newPower", required = true) newPower: Double,
    ): ElectricConsumerDto {
        logger.trace { "Switch a consumer by id - $elConsumerId power - $newPower" }
        val consumer = electricConsumerService.switchElectricConsumer(elConsumerId, newPower)
            .let(roomMapper::toElectricConsumerDto)
        logger.trace { "Switched the consumer - $consumer" }
        return consumer
    }

    companion object : KLogging() {
        const val ROOT_URI = "/rooms"
        const val LIGHT_SWITCHERS = "/electric-consumers"
    }
}
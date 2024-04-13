package com.example.townservice.mappers

import com.example.townservice.dtos.RoomDto
import com.example.townservice.dtos.consumers.ElectricConsumerDto
import com.example.townservice.models.Room
import com.example.townservice.models.consumers.ElectricConsumer
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper
interface RoomMapper {
    @Mapping(source = "electricConsumers", target = "electricConsumersDto", qualifiedByName = ["toElectricConsumersDto"])
    fun toRoomDto(room: Room): RoomDto

    @Mapping(source = "electricConsumersDto", target = "electricConsumers", qualifiedByName = ["toElectricConsumers"])
    fun toRoom(roomDto: RoomDto): Room

    @Named("toElectricConsumersDto")
    fun toElectricConsumersDto(electricConsumers: MutableList<ElectricConsumer>): MutableList<ElectricConsumerDto>

    @Named("toElectricConsumers")
    fun toElectricConsumers(electricConsumersDto: MutableList<ElectricConsumerDto>): MutableList<ElectricConsumer>

    fun toElectricConsumerDto(electricConsumer: ElectricConsumer): ElectricConsumerDto
    fun toElectricConsumer(electricConsumerDto: ElectricConsumerDto): ElectricConsumer
}
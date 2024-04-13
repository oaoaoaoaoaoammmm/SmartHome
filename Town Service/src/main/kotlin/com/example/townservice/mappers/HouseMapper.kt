package com.example.townservice.mappers

import com.example.townservice.dtos.HouseDto
import com.example.townservice.models.House
import org.mapstruct.Mapper

@Mapper
interface HouseMapper {
    fun toHouseDto(house: House): HouseDto
    fun toHouse(houseDto: HouseDto): House
}
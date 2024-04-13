package com.example.townservice.mappers

import com.example.townservice.dtos.TariffDto
import com.example.townservice.dtos.TownDto
import com.example.townservice.dtos.WeatherDto
import com.example.townservice.models.Tariff
import com.example.townservice.models.Town
import com.example.townservice.models.Weather
import org.mapstruct.Mapper

@Mapper
interface TownMapper {
    fun toTownDto(town: Town): TownDto
    fun toTown(townDto: TownDto): Town
    fun toWeatherDto(weather: Weather): WeatherDto
    fun toWeather(weatherDto: WeatherDto): Weather
    fun toTariffDto(tariff: Tariff): TariffDto
    fun toTariff(tariffDto: TariffDto): Tariff
}
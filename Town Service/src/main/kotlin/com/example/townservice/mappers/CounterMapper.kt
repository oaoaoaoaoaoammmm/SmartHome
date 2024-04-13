package com.example.townservice.mappers

import com.example.townservice.dtos.CounterDto
import com.example.townservice.models.Counter
import org.mapstruct.Mapper

@Mapper
interface CounterMapper {
    fun toCounterDto(counter: Counter): CounterDto
    fun toCounter(counterDto: CounterDto): Counter
}
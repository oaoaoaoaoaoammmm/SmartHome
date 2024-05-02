package com.example.townservice.controller

import com.example.townservice.configuration.annotation.RestMappingController
import com.example.townservice.controller.DataController.Companion.ROOT_URI
import com.example.townservice.dtos.HouseDto
import io.swagger.v3.oas.annotations.Hidden
import mu.KLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.client.RestTemplate

@Hidden
@RestMappingController(ROOT_URI)
class DataController {
    @PostMapping
    fun initData(): ResponseEntity<String> {

        for (i in 1..10000) {
            val dto = HttpEntity(HouseDto(null, "Marata $i", 0.5))
            var roomCount = 1
            roomCount = if (i % 6 !in 1 .. 5) 1
            else i % 6
            val restTemplate = RestTemplate()
                .exchange("http://localhost:8081/houses?townId=3f8b6677-ab42-4e89-96da-c8734230e309&roomCount=$roomCount", HttpMethod.POST, dto, HouseDto::class.java)
            println(restTemplate.body)
        }
        return ResponseEntity.ok("Инициализация данных прошла успешно")
    }


    companion object : KLogging() {
        const val ROOT_URI = "/data"
    }
}
package com.example.townservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

// TODO
//      1) Сбор логов через файл и их отображение в Spring Boot Admin
//      2) Констрэинты в базе данных не работают

@EnableDiscoveryClient
@SpringBootApplication
class TownServiceApplication

fun main(args: Array<String>) {
    runApplication<TownServiceApplication>(*args)
}


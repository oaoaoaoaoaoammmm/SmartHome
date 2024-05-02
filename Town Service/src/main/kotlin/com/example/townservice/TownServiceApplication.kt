package com.example.townservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient


@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
class TownServiceApplication

fun main(args: Array<String>) {
    runApplication<TownServiceApplication>(*args)
}


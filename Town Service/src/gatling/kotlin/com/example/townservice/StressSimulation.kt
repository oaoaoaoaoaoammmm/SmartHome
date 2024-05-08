package com.example.townservice

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl
import io.gatling.javaapi.http.HttpDsl.http
import java.time.Duration

class StressSimulation : Simulation() {

    private val httpProtocol = http
        .baseUrl("http://localhost:8080/api/town")
        .acceptHeader("application/json")

    //////////////////////////////////////////////////////////////////////

    private val getTownsRequest = scenario("Find all towns")
        .exec(
            http("")
                .get("/towns")
                .check(HttpDsl.status().`is`(200))
        )

    //////////////////////////////////////////////////////////////////////

    private val weatherFeeder = csv("weather.csv").circular()

    private val getWeatherByTownRequest = scenario("Find weather by town")
        .feed(weatherFeeder)
        .exec(
            http("")
                .get("/towns/#{town_id}/weather")
                .check(HttpDsl.status().`is`(200))
        )

    //////////////////////////////////////////////////////////////////////

    private val tariffFeeder = csv("tariff.csv").circular()

    private val getTariffsByTownRequest = scenario("Find tariff by town")
        .feed(tariffFeeder)
        .exec(
            http("")
                .get("/towns/#{town_id}/tariffs")
                .check(HttpDsl.status().`is`(200))
        )

    //////////////////////////////////////////////////////////////////////

    private val houseFeeder = csv("house.csv").circular()

    private val getHousesByTownRequest = scenario("Find page of house by town")
        .feed(houseFeeder)
        .exec(
            http("")
                .get("/houses?townId=#{town_id}&page=0&size=10")
                .check(HttpDsl.status().`is`(200))
        )

    private val getHouseByIdRequest = scenario("Find house by id")
        .feed(houseFeeder)
        .exec(
            http("")
                .get("/houses/#{id}")
                .check(HttpDsl.status().`is`(200))
        )

    //////////////////////////////////////////////////////////////////////

    private val counterFeeder = csv("counter.csv").circular()

    private val getCountersByHouseRequest = scenario("Find counters by house")
        .feed(counterFeeder)
        .exec(
            http("")
                .get("/counters?houseId=#{house_id}")
                .check(HttpDsl.status().`is`(200))
        )

    //////////////////////////////////////////////////////////////////////

    private val receiptFeeder = csv("receipt.csv").circular()

    private val getReceiptsByHouse = scenario("Get receipts by house")
        .feed(receiptFeeder)
        .exec(
            http("")
                .get("/receipts?houseId=#{house_id}&page=0&size=5")
                .check(HttpDsl.status().`is`(200))
        )

    private val createReceiptsByHouse = scenario("Create receipts by house")
        .feed(houseFeeder)
        .exec(
            http("")
                .post("/receipts?houseId=#{id}")
                .check(HttpDsl.status().`is`(201))
        )

    //////////////////////////////////////////////////////////////////////

    private val roomFeeder = csv("room.csv").circular()

    private val getRoomsByHouse = scenario("Get rooms by house")
        .feed(roomFeeder)
        .exec(
            http("")
                .get("/rooms?houseId=#{house_id}")
                .check(HttpDsl.status().`is`(200))
        )

    private val getRoomById = scenario("Get room by id")
        .feed(roomFeeder)
        .exec(
            http("")
                .get("/rooms/#{id}")
                .check(HttpDsl.status().`is`(200))
        )

    //////////////////////////////////////////////////////////////////////

    private val electricConsumerFeeder = csv("electric_consumer.csv").random()

    private val switchElectricConsumer = scenario("Switch consumer by id")
        .feed(electricConsumerFeeder)
        .exec(
            http("")
                .patch("/rooms/electric-consumers/#{id}?newPower=50")
                .check(HttpDsl.status().`is`(205))
        )

    init {
        this
            .setUp(
                getTownsRequest.injectOpen(rampUsers(1650).during(Duration.ofSeconds(18))),
                getWeatherByTownRequest.injectOpen(rampUsers(1650).during(Duration.ofSeconds(18))),
                getTariffsByTownRequest.injectOpen(rampUsers(1600).during(Duration.ofSeconds(18))),
                getHousesByTownRequest.injectOpen(rampUsers(1600).during(Duration.ofSeconds(18))),
                getHouseByIdRequest.injectOpen(rampUsers(1600).during(Duration.ofSeconds(18))),
                getCountersByHouseRequest.injectOpen(rampUsers(1000).during(Duration.ofSeconds(18))),
                getReceiptsByHouse.injectOpen(rampUsers(1600).during(Duration.ofSeconds(18))),
                getRoomsByHouse.injectOpen(rampUsers(1600).during(Duration.ofSeconds(18))),
                getRoomById.injectOpen(rampUsers(1700).during(Duration.ofSeconds(18))),
                switchElectricConsumer.injectOpen(rampUsers(1000).during(Duration.ofSeconds(18)))
            )
            .protocols(httpProtocol)
    }
}
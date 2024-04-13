package com.example.townservice

import com.example.townservice.config.PostgresAutoConfiguration
import com.example.townservice.consts.INTEGRATION
import com.example.townservice.repositories.*
import com.example.townservice.utils.SPRINGFIELD_ELECTRIC_TARIFF
import com.example.townservice.utils.SPRINGFIELD_TOWN
import com.example.townservice.utils.SPRINGFIELD_WEATHER
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [PostgresAutoConfiguration::class]
)
@Tag(INTEGRATION)
@ActiveProfiles("test")
class BaseIntegrationTest {

    @Autowired
    private lateinit var weatherRepository: WeatherRepository

    @Autowired
    private lateinit var townRepository: TownRepository

    @Autowired
    private lateinit var houseRepository: HouseRepository

    @Autowired
    private lateinit var counterRepository: CounterRepository

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @Autowired
    private lateinit var tariffRepository: TariffRepository

    @Autowired
    private lateinit var electricConsumerRepository: ElectricConsumerRepository

    @Autowired
    private lateinit var receiptRepository: ReceiptRepository


    protected lateinit var mockMvc: MockMvc

    private val objectMapper: ObjectMapper = jacksonObjectMapper().findAndRegisterModules()
    protected fun Any.toJson(): String = objectMapper.writeValueAsString(this)

    @BeforeEach
    fun setUp(context: WebApplicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build()
        initTestData()
    }

    fun initTestData() {
        townRepository.save(SPRINGFIELD_TOWN)
        weatherRepository.save(SPRINGFIELD_WEATHER)
        tariffRepository.save(SPRINGFIELD_ELECTRIC_TARIFF)
    }

    companion object {
        const val APPLICATION_JSON = "application/json"
    }
}

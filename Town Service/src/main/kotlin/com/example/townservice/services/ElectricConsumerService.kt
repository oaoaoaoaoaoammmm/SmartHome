package com.example.townservice.services

import com.example.townservice.configuration.ClockConfiguration
import com.example.townservice.models.consumers.ElectricConsumer
import com.example.townservice.models.enumerations.ElectricConsumerType
import com.example.townservice.repositories.ElectricConsumerRepository
import mu.KLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import kotlin.math.abs

@Service
class ElectricConsumerService(
    private val electricConsumerRepository: ElectricConsumerRepository,
    private val clockConfiguration: ClockConfiguration
) {

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    fun switchElectricConsumer(elConsumerId: UUID, newPower: Double): ElectricConsumer {
        logger.info { "Switch electric consumer id - $elConsumerId power - $newPower" }
        val elConsumer = electricConsumerRepository.findById(elConsumerId)
            .orElseThrow { throw NoSuchElementException("Can't find electric consumer") }

        val currTime = LocalDateTime.now(clockConfiguration.clock())

        with(elConsumer) {
            // 1 hour = 1 minute
            counter!!.value += abs((currTime.second - timeLastSwitchOn.second)) / 60.0 * electricPower
            electricPower =
                if (electricConsumerType != ElectricConsumerType.LIGHT_SWITCHER) newPower
                else if (newPower > 10.0) 10.0 else newPower
            elConsumer.timeLastSwitchOn = currTime
        }

        return elConsumer
    }

    fun addElectricConsumers(switchers: Collection<ElectricConsumer>): Collection<ElectricConsumer> {
        logger.info { "Add electric consumers - $switchers" }
        return electricConsumerRepository.saveAll(switchers)
    }

    private companion object : KLogging()
}
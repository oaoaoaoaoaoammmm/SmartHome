package com.example.townservice.services

import com.example.townservice.models.Counter
import com.example.townservice.models.enumerations.CommunalType
import com.example.townservice.repositories.CounterRepository
import mu.KLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

@Service
class CounterService(
    private val counterRepository: CounterRepository
) {

    @Cacheable(cacheNames = ["countersCache"])
    fun findAllCountersInHouse(houseId: UUID):Collection<Counter> {
        logger.info { "Find counters in a house id - $houseId" }
        counterRepository.callCalcCounterValue(houseId)
        return counterRepository.findAllByHouseId(houseId)
    }

    fun findCounter(counterId: UUID): Counter {
        logger.info { "Find counter by id - $counterId" }
        return counterRepository.findById(counterId)
            .orElseThrow { throw NoSuchElementException("Can't find counter") }
    }

    fun findCounter(houseId: UUID, communalType: CommunalType): Counter {
        logger.info { "Find counter by house id - $houseId communal type - $communalType" }
        return counterRepository.findCounterByHouseIdAndCommunalType(houseId, communalType)
            .orElseThrow { throw NoSuchElementException("Can't find counter") }
    }

    @CacheEvict(cacheNames = ["countersCache"], allEntries = true)
    fun addCounters(counters: Collection<Counter>): Collection<Counter> {
        logger.info { "Add counters - $counters" }
        return counterRepository.saveAll(counters)
    }

    private companion object : KLogging()
}
package com.example.townservice.services

import com.example.townservice.models.Tariff
import com.example.townservice.models.Town
import com.example.townservice.models.enumerations.CommunalType
import com.example.townservice.repositories.TariffRepository
import mu.KLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TariffService(
    private val tariffRepository: TariffRepository
) {

    fun findAllTariffs(): Collection<Tariff> {
        logger.info { "Find tariffs" }
        return tariffRepository.findAll()
    }

    fun findTariffsByTown(townId: UUID): Collection<Tariff> {
        logger.info { "Find tariffs by town id - $townId" }
        return tariffRepository.findByTownId(townId)
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    fun updateTariff(tariffId: UUID, tariff: Tariff): Tariff {
        logger.info { "Update tariff by id - $tariffId tariff - $tariff" }
        val oldTariff = tariffRepository.findById(tariffId)
            .orElseThrow { throw NoSuchElementException("Can't find a tariff") }

        with(oldTariff) {
            cost = tariff.cost
        }

        return oldTariff
    }

    fun saveTariffs(tariffs: Collection<Tariff>): Collection<Tariff> {
        logger.info { "Save tariffs - $tariffs" }
        return tariffRepository.saveAll(tariffs)
    }

    fun findTariffById(tariffId: UUID): Tariff {
        logger.info { "Find tariff by id - $tariffId" }
        return tariffRepository.findById(tariffId)
            .orElseThrow { throw NoSuchElementException("Can't find a tariff") }
    }

    fun findTariffByTypeAndTown(tariffType: CommunalType, town: Town): Tariff {
        logger.info { "Find tariff by town - $town tariff type - $tariffType" }
        return tariffRepository.findByCommunalTypeAndTown(tariffType, town)
            .orElseThrow { throw NoSuchElementException("Can't find a tariff") }
    }

    private companion object : KLogging()
}
package com.example.townservice.services

import com.example.townservice.configuration.ClockConfiguration
import com.example.townservice.models.Counter
import com.example.townservice.models.Receipt
import com.example.townservice.repositories.ReceiptRepository
import mu.KLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class ReceiptService(
    private val counterService: CounterService,
    private val tariffService: TariffService,
    private val receiptRepository: ReceiptRepository,
    private val clockConfiguration: ClockConfiguration
) {

    //@Cacheable(cacheNames = ["receiptsCache"])
    fun findPageReceiptsInHouse(houseId: UUID, pageable: Pageable): Page<Receipt> {
        logger.info { "Find page of receipts by house id - $houseId page number - ${pageable.pageNumber}" }
        return receiptRepository.findPageByHouseId(houseId, pageable)
    }

    //@CacheEvict(cacheNames = ["receiptsCache"], allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    fun createReceipts(houseId: UUID): Collection<Receipt> {
        logger.info { "Create receipts for house by id - $houseId" }
        receiptRepository.callCalcCounterValue(houseId)

        val receipts = counterService.findAllCountersInHouse(houseId)
            .map { counter ->
                val house = counter.house!!
                val prevReceipt =
                    receiptRepository.findReceiptByCommunalTypeAndByHouseIdAndLaterDate(
                        house,
                        counter.communalType
                    ).orElse(getClearReceipt(counter))
                val tariff = tariffService.findTariffByTypeAndTown(
                    counter.communalType,
                    house.town!!
                )
                val debt = tariff.cost * (counter.value - prevReceipt.currentCounterValue)
                Receipt(
                    payment = false,
                    communalType = counter.communalType,
                    debt = debt,
                    currentCounterValue = counter.value,
                    previousCounterValue = prevReceipt.currentCounterValue,
                    date = LocalDateTime.now(clockConfiguration.clock()),
                    id = null,
                    house = house
                )
            }
        return receiptRepository.saveAll(receipts)
    }

    //@CacheEvict(cacheNames = ["receiptsCache"], allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    fun payReceipt(receiptId: UUID): Receipt {
        logger.info { "Pay receipt by id - $receiptId" }
        val receipt = receiptRepository.findById(receiptId)
            .orElseThrow { throw NoSuchElementException("Can't find a receipt") }

        with(receipt) {
            payment = true
        }

        return receipt
    }

    private fun getClearReceipt(counter: Counter): Receipt {
        return Receipt(
            payment = false,
            communalType = counter.communalType,
            debt = 0.0,
            currentCounterValue = 0.0,
            previousCounterValue = 0.0,
            date = LocalDateTime.now(clockConfiguration.clock()),
            id = null,
            house = counter.house
        )
    }

    private companion object : KLogging()
}
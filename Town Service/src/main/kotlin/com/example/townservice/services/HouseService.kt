package com.example.townservice.services

import com.example.townservice.configuration.ClockConfiguration
import com.example.townservice.models.Counter
import com.example.townservice.models.House
import com.example.townservice.models.Room
import com.example.townservice.models.consumers.ElectricConsumer
import com.example.townservice.models.enumerations.CommunalType
import com.example.townservice.models.enumerations.ElectricConsumerType
import com.example.townservice.repositories.HouseRepository
import mu.KLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class HouseService(
    private val townService: TownService,
    private val roomService: RoomService,
    private val counterService: CounterService,
    private val electricConsumerService: ElectricConsumerService,
    private val houseRepository: HouseRepository,
    private val clockConfiguration: ClockConfiguration
) {

    fun findAllHousesInTown(townId: UUID): Collection<House> {
        logger.info { "Find houses in a town by id - $townId" }
        return houseRepository.findAllByTownId(townId)
    }

    fun findPageHousesInTown(townId: UUID, pageable: Pageable): Page<House> {
        logger.info { "Find a page of houses by town id - $townId" }
        return houseRepository.findAllByTownId(townId, pageable)
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    fun addHouseInTown(townId: UUID, house: House, roomCount: Int): House {
        logger.info { "Add a house in a town by id - $townId, house - $house room count - $roomCount" }
        townService.findTown(townId).also { house.town = it }
        houseRepository.save(house)

        val counters = CommunalType.entries
            .map { type ->
                Counter(
                    value = 0.0,
                    communalType = type,
                    id = null,
                    house = house
                )
            }
        counterService.addCounters(counters)

        var count = roomCount
        val rooms: MutableList<Room> = mutableListOf()
        while (count != 0) {
            rooms.add(
                Room(
                    id = null,
                    house = house,
                    electricConsumers = ArrayList(ElectricConsumerType.entries
                        .map { type ->
                            ElectricConsumer(
                                id = null,
                                timeLastSwitchOn = LocalDateTime.now(clockConfiguration.clock()),
                                timeLastSwitchOff = LocalDateTime.now(clockConfiguration.clock()),
                                electricPower = 0.0,
                                electricConsumerType = type,
                                counter = counters.first { it.communalType == CommunalType.ELECTRIC }
                            )
                        }
                    )
                )
            )
            count--
        }

        val electricConsumers = mutableListOf<ElectricConsumer>()
        rooms.forEach { electricConsumers.addAll(it.electricConsumers) }
        electricConsumerService.addElectricConsumers(electricConsumers)
        roomService.addRooms(rooms)

        return house
    }

    fun deleteHouse(houseId: UUID) {
        logger.info { "Delete a house by id - $houseId" }
        houseRepository.deleteById(houseId)
    }

    fun findHouse(houseId: UUID): House {
        logger.info { "Find a house by id - $houseId" }
        return houseRepository.findById(houseId)
            .orElseThrow { throw NoSuchElementException("Can't find house") }
    }

    private companion object : KLogging()
}

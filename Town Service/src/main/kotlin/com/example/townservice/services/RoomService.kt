package com.example.townservice.services

import com.example.townservice.models.Room
import com.example.townservice.repositories.RoomRepository
import mu.KLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoomService(
    private val roomRepository: RoomRepository,
) {

    //@Cacheable(cacheNames = ["roomsCache"])
    fun findAllRoomsInHouse(houseId: UUID): Collection<Room> {
        logger.info { "Find rooms in a house - $houseId" }
        return roomRepository.findAllByHouseId(houseId)
    }

    //@Cacheable(cacheNames = ["roomsCache"])
    fun findRoom(roomId: UUID): Room {
        logger.info { "Find room by id - $roomId" }
        return roomRepository.findById(roomId)
            .orElseThrow { throw NoSuchElementException("Can't find room") }
    }

    //@CacheEvict(cacheNames = ["roomsCache"], allEntries = true)
    fun addRooms(rooms: Collection<Room>): Collection<Room> {
        logger.info { "Add rooms - $rooms" }
        return roomRepository.saveAll(rooms)
    }

    private companion object : KLogging()
}
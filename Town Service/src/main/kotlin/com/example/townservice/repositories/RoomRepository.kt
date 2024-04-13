package com.example.townservice.repositories

import com.example.townservice.models.Room
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoomRepository : JpaRepository<Room, UUID> {
    fun findAllByHouseId(houseId: UUID): Collection<Room>
}
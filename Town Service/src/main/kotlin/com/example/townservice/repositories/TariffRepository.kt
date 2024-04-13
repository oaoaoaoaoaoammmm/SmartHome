package com.example.townservice.repositories

import com.example.townservice.models.Tariff
import com.example.townservice.models.Town
import com.example.townservice.models.enumerations.CommunalType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface TariffRepository: JpaRepository<Tariff, UUID> {
    fun findByCommunalTypeAndTown(tariffType: CommunalType, town: Town): Optional<Tariff>
    fun findByTownId(townId: UUID): Collection<Tariff>
}
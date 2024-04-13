package com.example.townservice.repositories

import com.example.townservice.models.Counter
import com.example.townservice.models.enumerations.CommunalType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface CounterRepository: JpaRepository<Counter, UUID> {
    fun findAllByHouseId(houseId: UUID): Collection<Counter>
    fun findCounterByHouseIdAndCommunalType(houseId: UUID, communalType: CommunalType): Optional<Counter>
    @Procedure(procedureName = "calc_counter_value")
    fun callCalcCounterValue(houseId: UUID): Void
}
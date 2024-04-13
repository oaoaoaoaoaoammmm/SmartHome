package com.example.townservice.repositories

import com.example.townservice.models.House
import com.example.townservice.models.Receipt
import com.example.townservice.models.enumerations.CommunalType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReceiptRepository :
    PagingAndSortingRepository<Receipt, UUID>,
    JpaRepository<Receipt, UUID> {
    fun findPageByHouseId(houseId: UUID, pageable: Pageable): Page<Receipt>

    @Query(
        value = """
        select receipt from Receipt receipt
        where receipt.house = :house 
            and receipt.communalType = :communalType 
            and receipt.date = (select max(r.date) from Receipt r)  
    """
    )
    fun findReceiptByCommunalTypeAndByHouseIdAndLaterDate(house: House, communalType: CommunalType): Optional<Receipt>

    @Procedure(procedureName = "calc_counter_value")
    fun callCalcCounterValue(houseTownId: UUID): Void
}
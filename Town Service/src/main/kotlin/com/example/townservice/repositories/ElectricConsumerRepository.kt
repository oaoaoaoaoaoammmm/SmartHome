package com.example.townservice.repositories

import com.example.townservice.models.consumers.ElectricConsumer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ElectricConsumerRepository : JpaRepository<ElectricConsumer, UUID>
package com.example.townservice.repositories

import com.example.townservice.models.Town
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TownRepository : JpaRepository<Town, UUID>

package com.example.townservice.controllers

import com.example.townservice.BaseIntegrationTest
import com.example.townservice.controller.TownController.Companion.ROOT_URI
import com.example.townservice.dtos.TownDto
import com.example.townservice.utils.SPRINGFIELD_TOWN
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class TownControllerTest : BaseIntegrationTest() {

    @Test
    fun `add town`() {
        mockMvc.perform(
            post("/$ROOT_URI")
                .contentType(APPLICATION_JSON)
                .content(
                    TownDto(
                        id = null,
                        name = "SSP"
                    ).toJson()
                )
        ).andExpect(status().isCreated)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `find all town`() {
        mockMvc.perform(
            get("/$ROOT_URI")
        ).andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `find town by id`() {
        mockMvc.perform(
            get("/$ROOT_URI" + "/" + SPRINGFIELD_TOWN.id)
        ).andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `delete town by id`() {
        mockMvc.perform(
            delete("/$ROOT_URI" + "/" + SPRINGFIELD_TOWN.id)
        ).andExpect(status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }
}
package com.example.townservice.controller

import com.example.townservice.configuration.annotation.RestMappingController
import com.example.townservice.controller.ReceiptController.Companion.ROOT_URI
import com.example.townservice.dtos.ReceiptDto
import com.example.townservice.mappers.ReceiptMapper
import com.example.townservice.services.ReceiptService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.NotNull
import mu.KLogging
import org.springdoc.core.converters.models.PageableAsQueryParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.SortDefault
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Validated
@Tag(name = "Receipt's controller")
@RestMappingController(ROOT_URI)
class ReceiptController(
    private val receiptService: ReceiptService,
    private val receiptMapper: ReceiptMapper
) {

    @Operation(summary = "Find receipt's page in a house")
    @GetMapping
    @PageableAsQueryParam
    fun findPageReceiptsInHouse(
        @RequestParam @NotNull @Parameter(name = "houseId", required = true) houseId: UUID,
        @Schema(hidden = true) @SortDefault("payment", direction = Sort.Direction.DESC) @PageableDefault pageable: Pageable
    ): Page<ReceiptDto> {
        logger.trace { "Find page of receipt's by house id - $houseId page number - ${pageable.pageNumber}" }
        val page = receiptService.findPageReceiptsInHouse(houseId, pageable)
            .map(receiptMapper::toReceiptDto)
        logger.trace { "Found page of receipts - ${page.content}" }
        return page
    }

    @Operation(summary = "Create receipts for house")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createReceiptsForHouse(
        @RequestParam @NotNull @Parameter(name = "houseId", required = true) houseId: UUID
    ): Collection<ReceiptDto> {
        logger.trace { "Create receipts for a house by id - $houseId" }
        val receipts = receiptService.createReceipts(houseId)
            .map(receiptMapper::toReceiptDto)
        logger.trace { "Created receipts for the house id - $houseId receipt - $receipts" }
        return receipts
    }

    @Operation(summary = "Pay receipt")
    @PatchMapping("/{receiptId}")
    @ResponseStatus(HttpStatus.OK)
    fun payReceipt(
        @PathVariable @NotNull receiptId: UUID
    ): ReceiptDto {
        logger.trace { "Pay for a receipt by id - $receiptId" }
        val receipt = receiptService.payReceipt(receiptId)
            .let(receiptMapper::toReceiptDto)
        logger.trace { "The receipt payed id - ${receipt.id}" }
        return receipt
    }

    companion object :KLogging() {
        const val ROOT_URI = "/receipts"
    }
}
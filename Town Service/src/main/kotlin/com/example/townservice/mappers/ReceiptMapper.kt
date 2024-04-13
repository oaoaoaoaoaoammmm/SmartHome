package com.example.townservice.mappers

import com.example.townservice.dtos.ReceiptDto
import com.example.townservice.models.Receipt
import org.mapstruct.Mapper

@Mapper
interface ReceiptMapper {
    fun toReceiptDto(receipt: Receipt): ReceiptDto
    fun toReceipt(receiptDto: ReceiptDto): Receipt
}
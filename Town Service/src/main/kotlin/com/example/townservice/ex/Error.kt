package com.example.townservice.ex

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.ResponseEntity
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Error(
    val errorId: String = UUID.randomUUID().toString(),
    val status: Int,
    val code: ErrorCode
)

fun Error.asResponseEntity(): ResponseEntity<Error> {
    return ResponseEntity
        .status(this.status)
        .body(this)
}

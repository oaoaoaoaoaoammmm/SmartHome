package com.example.townservice.ex

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestControllerAdvice {

    @ExceptionHandler(
        value = [
            ConstraintViolationException::class,
            MethodArgumentNotValidException::class
        ]
    )
    fun handleValidationExceptions(exception: Exception): ResponseEntity<Error> {
        return Error(
            status = HttpStatus.BAD_REQUEST.value(),
            code = ErrorCode.INVALID_REQUEST
        ).asResponseEntity()
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchException(exception: Exception): ResponseEntity<Error> {
        return Error(
            status = HttpStatus.BAD_REQUEST.value(),
            code = ErrorCode.NO_SUCH_ELEMENT
        ).asResponseEntity()
    }

    /*
    @ExceptionHandler(Exception::class)
    fun defaultException(exception: Exception): ResponseEntity<Error> {
        return Error(
            status = HttpStatus.BAD_REQUEST.value(),
            code = ErrorCode.SERVICE_UNAVAILABLE
        ).asResponseEntity()
    }
     */
}
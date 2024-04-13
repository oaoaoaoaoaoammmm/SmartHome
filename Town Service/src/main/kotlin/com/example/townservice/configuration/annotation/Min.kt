package com.example.townservice.configuration.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.VALUE_PARAMETER,
)
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MinValidator::class])
annotation class Min(
    val min: Double,
    val message: String = "Invalid field range",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class MinValidator : ConstraintValidator<Min, Double> {

    private lateinit var message: String
    private var min: Double = Double.MIN_VALUE

    override fun initialize(constraintAnnotation: Min?) {
        super.initialize(constraintAnnotation!!)
        message = constraintAnnotation.message
        min = constraintAnnotation.min
    }

    override fun isValid(value: Double?, context: ConstraintValidatorContext?): Boolean {
        return value!! >= min
    }

}

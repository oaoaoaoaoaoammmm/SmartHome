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
@Constraint(validatedBy = [DoubleRangeValidator::class])
annotation class DoubleRange(
    val min: Double,
    val max: Double,
    val message: String = "Invalid field range",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class DoubleRangeValidator : ConstraintValidator<DoubleRange, Double> {

    private lateinit var message: String
    private var min: Double = Double.MIN_VALUE
    private var max: Double = Double.MAX_VALUE

    override fun initialize(constraintAnnotation: DoubleRange?) {
        super.initialize(constraintAnnotation!!)
        message = constraintAnnotation.message
        min = constraintAnnotation.min
        max = constraintAnnotation.max
    }

    override fun isValid(value: Double?, context: ConstraintValidatorContext?): Boolean {
        return value!! in min..max
    }
}

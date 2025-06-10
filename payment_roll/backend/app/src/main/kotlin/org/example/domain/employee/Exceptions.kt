package org.example.domain.employee

sealed class EmployeeExceptions(
    override val message: String
) : RuntimeException(message) {

    class NotFound : RuntimeException("Employee was not found")
}
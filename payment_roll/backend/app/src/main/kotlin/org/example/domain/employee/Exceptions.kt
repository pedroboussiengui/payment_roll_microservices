package org.example.domain.employee

sealed class EmployeeExceptions(
    override val message: String
) : RuntimeException(message) {

    class NotFound : RuntimeException("Employee was not found")

    class UnicityViolation(reason: String) : RuntimeException(reason)

    class BusinessRuleViolation(reason: String, val violations: List<String> = emptyList()) :
        RuntimeException(reason)
}

sealed class ContractExceptions(
    override val message: String
) : RuntimeException(message) {

    class ContractNotFound : RuntimeException("Contract was not found")

    class UnableHandleEvent(reason: String) : RuntimeException(reason)
}
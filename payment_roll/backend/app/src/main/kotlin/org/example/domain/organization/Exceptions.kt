package org.example.domain.organization

sealed class OrganizationExceptions(
    override val message: String
) : RuntimeException(message) {

    class NotFound : RuntimeException("Organization was not found")

    class UnicityViolation(reason: String) : RuntimeException(reason)

    class BusinessRuleViolation(reason: String, val violations: List<String> = emptyList()) :
        RuntimeException(reason)
}
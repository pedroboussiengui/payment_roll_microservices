package org.example.infra.ktor.exceptionsHandler

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.response.respond
import org.example.domain.organization.OrganizationExceptions

fun StatusPagesConfig.organizationExceptions() {
    exception<OrganizationExceptions.NotFound> { call, cause ->
        call.respond(HttpStatusCode.NotFound, Problem(
            title = "Organization not found",
            detail = cause.message ?: "Organization not found",
            status = HttpStatusCode.NotFound.value
        ))
    }
    exception<OrganizationExceptions.BusinessRuleViolation> { call, cause ->
        call.respond(HttpStatusCode.UnprocessableEntity, Problem(
            title = "Business rules violations",
            detail = cause.message!!,
            status = HttpStatusCode.UnprocessableEntity.value,
            errors = cause.violations
        ))
    }
}
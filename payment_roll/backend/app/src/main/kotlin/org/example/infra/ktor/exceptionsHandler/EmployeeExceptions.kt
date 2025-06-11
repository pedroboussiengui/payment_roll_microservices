package org.example.infra.ktor.exceptionsHandler

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.response.respond
import org.example.domain.employee.EmployeeExceptions

fun StatusPagesConfig.employeeExceptions() {
    exception<EmployeeExceptions.NotFound> { call, cause ->
        call.respond(HttpStatusCode.NotFound, Problem(
            title = "Employee not found",
            detail = cause.message ?: "Employee not found",
            status = HttpStatusCode.NotFound.value
        ))
    }
    exception<EmployeeExceptions.BusinessRuleViolation> { call, cause ->
        call.respond(HttpStatusCode.UnprocessableEntity, Problem(
            title = "Business rules violations",
            detail = cause.message!!,
            status = HttpStatusCode.UnprocessableEntity.value,
            errors = cause.violations
        ))
    }
    exception<EmployeeExceptions.UnicityViolation> { call, cause ->
        call.respond(HttpStatusCode.Conflict, Problem(
            title = "Unicity principle violation",
            detail = cause.message!!,
            status = HttpStatusCode.Conflict.value
        ))
    }
}
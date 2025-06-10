package org.example.infra.ktor.exceptionsHandler

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.response.respond
import org.example.domain.employee.EmployeeExceptions

fun StatusPagesConfig.employeeExceptions() {
    exception<EmployeeExceptions.NotFound> { call, cause ->
        call.respond(HttpStatusCode.NotFound, Problem(
            title = "NotFound",
            detail = cause.message ?: "Employee not found",
            status = HttpStatusCode.NotFound.value
        ))
    }
}
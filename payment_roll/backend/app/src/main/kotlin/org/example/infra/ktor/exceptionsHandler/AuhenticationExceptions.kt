package org.example.infra.ktor.exceptionsHandler

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.response.respond
import org.example.application.exceptions.AuthenticationException

fun StatusPagesConfig.authenticationExceptions() {
    exception<AuthenticationException.MissingToken> { call, cause ->
        call.respond(HttpStatusCode.Unauthorized, Problem(
            title = "Unauthorized",
            detail = cause.message!!,
            status = HttpStatusCode.Unauthorized.value
        ))
    }
    exception<AuthenticationException.ExpiredToken> { call, cause ->
        call.respond(HttpStatusCode.Unauthorized, Problem(
            title = "Unauthorized",
            detail = cause.message!!,
            status = HttpStatusCode.Unauthorized.value
        ))
    }
    exception<AuthenticationException.InvalidToken> { call, cause ->
        call.respond(HttpStatusCode.Unauthorized, Problem(
            title = "Unauthorized",
            detail = cause.message!!,
            status = HttpStatusCode.Unauthorized.value
        ))
    }
}
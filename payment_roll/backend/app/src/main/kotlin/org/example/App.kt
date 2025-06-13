package org.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.example.application.exceptions.AuthenticationException
import org.example.application.usecase.employee.AddEmployee
import org.example.application.usecase.employee.AddEmployeeInput
import org.example.application.usecase.employee.ListEmployeeContracts
import org.example.application.usecase.employee.ListEmployees
import org.example.application.usecase.employee.RetrieveEmployeeByID
import org.example.infra.jwt.Auth0JwtService
import org.example.infra.ktor.exceptionsHandler.Problem
import org.example.infra.ktor.exceptionsHandler.authenticationExceptions
import org.example.infra.ktor.exceptionsHandler.employeeExceptions
import org.example.infra.ktor.routes.employeeRoute
import org.example.infra.ktor.routes.organizationRoute
import org.example.infra.repository.EmployeeDao

val ACCESS_TOKEN_KEY = AttributeKey<String>("AccessToken")
val JwtAuthPlugin = createRouteScopedPlugin("jwtAuthPlugin") {
    onCall { call ->
        val authHeader = call.request.headers["Authorization"]
        val accessToken = authHeader?.substringAfter("Bearer ")?.trim()
        if (accessToken.isNullOrBlank()) {
            throw AuthenticationException.MissingToken()
        }
        call.attributes.put(ACCESS_TOKEN_KEY, accessToken)
    }
}

fun main() {
    embeddedServer(Netty, port = 8081) {
        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = true
                explicitNulls = false
            })
        }
        install(CORS) {
            anyHost()
            anyMethod()
            allowNonSimpleContentTypes = true
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
        }
        install(JwtAuthPlugin)
        install(StatusPages) {
            authenticationExceptions()
            employeeExceptions()
            exception<BadRequestException> { call, cause ->
                call.respond(HttpStatusCode.BadRequest, Problem(
                    title = "BadRequest",
                    detail = cause.message ?: "Invalid request",
                    status = HttpStatusCode.BadRequest.value
                ))
            }
        }
        routing {
            employeeRoute()
            organizationRoute()
        }
    }.start(wait = true)
}

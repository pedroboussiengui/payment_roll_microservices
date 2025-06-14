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
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.example.application.exceptions.AuthenticationException
import org.example.infra.ktor.eventsModule
import org.example.infra.ktor.exceptionsHandler.Problem
import org.example.infra.ktor.exceptionsHandler.authenticationExceptions
import org.example.infra.ktor.exceptionsHandler.employeeExceptions
import org.example.infra.ktor.exceptionsHandler.organizationExceptions
import org.example.infra.ktor.routes.employeeRoute
import org.example.infra.ktor.routes.eventsRoute
import org.example.infra.ktor.routes.organizationRoute
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

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
                serializersModule = eventsModule
                classDiscriminator = "type"
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
            organizationExceptions()
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
            eventsRoute()
        }
    }.start(wait = true)
}

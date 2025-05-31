package org.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.Serializable

/**
 * RFC 9457 - “Problem Details for HTTP APIs”
 */
@Serializable
data class Problem(
    val title: String,
    val detail: String,
    val status: Int
)

fun main() {
    val employeeDao = EmployeeDao()
    val listEmployeeContracts = ListEmployeeContracts(employeeDao)
    val retrieveEmployeeByID = RetrieveEmployeeByID(employeeDao)
    val listEmployees = ListEmployees(employeeDao)
    val addEmployee = AddEmployee(employeeDao)

    LoadData(employeeDao)

    embeddedServer(Netty, port = 8081) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            anyHost()
            anyMethod()
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Authorization)
        }
        install(JwtAuthPlugin)
        install(StatusPages) {
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
        routing {
            get("/employees") {
                val accessToken = call.attributes[ACCESS_TOKEN_KEY]
                val output = listEmployees.execute(accessToken)
                call.respond(output)
            }
            get("/employees/{userId}") {
                val userId = call.parameters["userId"]
                val output = retrieveEmployeeByID.execute(userId!!)
                call.respond(output)
            }
            get("/employees/{userId}/contracts") {
                val userId = call.parameters["userId"]
                val output = listEmployeeContracts.execute(userId!!)
                call.respond(output)
            }
        }
    }.start(wait = true)
}

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

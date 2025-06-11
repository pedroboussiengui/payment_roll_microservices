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
import io.ktor.server.response.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.example.application.exceptions.AuthenticationException
import org.example.application.usecase.AddEmployee
import org.example.application.usecase.AddEmployeeInput
import org.example.application.usecase.ListEmployeeContracts
import org.example.application.usecase.ListEmployees
import org.example.application.usecase.RetrieveEmployeeByID
import org.example.infra.jwt.Auth0JwtService
import org.example.infra.ktor.exceptionsHandler.Problem
import org.example.infra.ktor.exceptionsHandler.authenticationExceptions
import org.example.infra.ktor.exceptionsHandler.employeeExceptions
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
    val employeeDao = EmployeeDao()
    val jwtService = Auth0JwtService()
    val listEmployeeContracts = ListEmployeeContracts(employeeDao)
    val retrieveEmployeeByID = RetrieveEmployeeByID(employeeDao, jwtService)
    val listEmployees = ListEmployees(employeeDao, jwtService)
    val addEmployee = AddEmployee(employeeDao, jwtService)

    LoadData(employeeDao)

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
            get("/employees") {
                val accessToken = call.attributes[ACCESS_TOKEN_KEY]
                val output = listEmployees.execute(accessToken)
                call.respond(output)
            }
            get("/employees/{employeeId}") {
                val accessToken = call.attributes[ACCESS_TOKEN_KEY]
                val employeeId = call.parameters["employeeId"]
                val output = retrieveEmployeeByID.execute(employeeId!!, accessToken)
                call.respond(output)
            }
            get("/employees/{employeeId}/contracts") {
                val employeeId = call.parameters["employeeId"]
                val output = listEmployeeContracts.execute(employeeId!!)
                call.respond(output)
            }
            post("/employees") {
                val accessToken = call.attributes[ACCESS_TOKEN_KEY]
                val input = call.receive<AddEmployeeInput>()
                val output = addEmployee.execute(input, accessToken)
                call.respond(HttpStatusCode.Created, output)
            }
        }
    }.start(wait = true)
}

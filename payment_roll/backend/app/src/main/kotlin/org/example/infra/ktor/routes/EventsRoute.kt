package org.example.infra.ktor.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.ACCESS_TOKEN_KEY
import org.example.application.usecase.events.Afastamento
import org.example.application.usecase.events.AfastamentoInput
import org.example.application.usecase.events.Retorno
import org.example.application.usecase.events.RetornoInput
import org.example.infra.jwt.Auth0JwtService
import org.example.infra.ktor.uuid
import org.example.infra.repository.employee.EmployeeRepositoryImpl

fun Route.eventsRoute() {
    val employeeRepository = EmployeeRepositoryImpl()
    val jwtService = Auth0JwtService()
    val afastamento = Afastamento(employeeRepository, jwtService)
    val retorno = Retorno(employeeRepository, jwtService)

    route("/employees/{employeeId}/contracts/{contractId}") {
        post("/afastamento") {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val employeeId = call.parameters.uuid("employeeId")
            val contractId = call.parameters.uuid("contractId")
            val input = call.receive<AfastamentoInput>()
            val output = afastamento.execute(employeeId!!, contractId!!, input, accessToken)
            call.respond(HttpStatusCode.OK, output)
        }
        post("/retorno") {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val employeeId = call.parameters.uuid("employeeId")
            val contractId = call.parameters.uuid("contractId")
            val input = call.receive<RetornoInput>()
            val output = retorno.execute(employeeId!!, contractId!!, input, accessToken)
            call.respond(HttpStatusCode.OK, output)
        }
    }
}
package org.example

import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate
import java.util.*



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
        routing {
            get("/employees") {
                val output = listEmployees.execute()
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

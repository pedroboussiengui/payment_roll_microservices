package org.example.infra.ktor.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.example.ACCESS_TOKEN_KEY
import org.example.application.usecase.employee.AddEmployee
import org.example.application.usecase.employee.AddEmployeeInput
import org.example.application.usecase.employee.ListEmployeeContracts
import org.example.application.usecase.employee.ListEmployees
import org.example.application.usecase.employee.RetrieveEmployeeByID
import org.example.infra.jwt.Auth0JwtService
import org.example.infra.repository.EmployeeDao

fun Route.employeeRoute() {
    val employeeDao = EmployeeDao()
    val jwtService = Auth0JwtService()
    val listEmployeeContracts = ListEmployeeContracts(employeeDao)
    val retrieveEmployeeByID = RetrieveEmployeeByID(employeeDao, jwtService)
    val listEmployees = ListEmployees(employeeDao, jwtService)
    val addEmployee = AddEmployee(employeeDao, jwtService)

    LoadData(employeeDao)

    route("/employees") {
        get {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val output = listEmployees.execute(accessToken)
            call.respond(output)
        }
        get("/{employeeId}") {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val employeeId = call.parameters["employeeId"]
            val output = retrieveEmployeeByID.execute(employeeId!!, accessToken)
            call.respond(output)
        }
        get("/{employeeId}/contracts") {
            val employeeId = call.parameters["employeeId"]
            val output = listEmployeeContracts.execute(employeeId!!)
            call.respond(output)
        }
        post {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val input = call.receive<AddEmployeeInput>()
            val output = addEmployee.execute(input, accessToken)
            call.respond(HttpStatusCode.Created, output)
        }
//        get("/{employeeId}/contracts/{contractId}") {
//            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
//        }
    }
}
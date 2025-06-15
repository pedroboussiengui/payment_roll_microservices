package org.example.infra.ktor.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.ACCESS_TOKEN_KEY
import org.example.application.usecase.employee.*
import org.example.infra.jwt.Auth0JwtService
import org.example.infra.ktor.uuid
import org.example.infra.repository.employee.EmployeeRepositoryImpl

fun Route.employeeRoute() {
    val employeeRepository = EmployeeRepositoryImpl()
    val jwtService = Auth0JwtService()
    val listEmployeeContracts = ListEmployeeContracts(employeeRepository, jwtService)
    val retrieveEmployeeByID = RetrieveEmployeeByID(employeeRepository, jwtService)
    val listEmployees = ListEmployees(employeeRepository, jwtService)
    val addEmployee = AddEmployee(employeeRepository, jwtService)
    val detailEmployeeContract = DetailEmployeeContract(employeeRepository, jwtService)
    val admission = Admission(employeeRepository, jwtService)
    val listContractEvents = ListContractEvents(employeeRepository, jwtService)

//    LoadData(employeeDao)

    route("/employees") {
        get {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val output = listEmployees.execute(accessToken)
            call.respond(output)
        }
        get("/{employeeId}") {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val employeeId = call.parameters.uuid("employeeId")
            val output = retrieveEmployeeByID.execute(employeeId!!, accessToken)
            call.respond(output)
        }
        get("/{employeeId}/contracts") {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val employeeId = call.parameters.uuid("employeeId")
            val output = listEmployeeContracts.execute(employeeId!!, accessToken)
            call.respond(output)
        }
        post {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val input = call.receive<AddEmployeeInput>()
            val output = addEmployee.execute(input, accessToken)
            call.respond(HttpStatusCode.Created, output)
        }
        get("/{employeeId}/contracts/{contractId}") {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val employeeId = call.parameters.uuid("employeeId")
            val contractId = call.parameters.uuid("contractId")
            val output = detailEmployeeContract.execute(employeeId!!, contractId!!, accessToken)
            call.respond(output)
        }
        post("/{employeeId}/admission") {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val employeeId = call.parameters.uuid("employeeId")
            val input = call.receive<AdmissionInput>()
            val output = admission.execute(employeeId!!, input, accessToken)
            call.respond(HttpStatusCode.Created, output)
        }
        get("/{employeeId}/contracts/{contractId}/events") {
            val accessToken = call.attributes[ACCESS_TOKEN_KEY]
            val employeeId = call.parameters.uuid("employeeId")
            val contractId = call.parameters.uuid("contractId")
            val output = listContractEvents.execute(employeeId!!, contractId!!, accessToken)
            call.respond(HttpStatusCode.OK, output)
        }
    }
}


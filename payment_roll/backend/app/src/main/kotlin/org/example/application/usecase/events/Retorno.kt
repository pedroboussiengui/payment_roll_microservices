package org.example.application.usecase.events

import kotlinx.serialization.Serializable
import org.example.domain.employee.AfastamentoEvent
import org.example.domain.employee.ContractEvent
import org.example.domain.employee.ContractExceptions
import org.example.domain.employee.ContractStateMachine
import org.example.domain.employee.Employee
import org.example.domain.employee.EmployeeExceptions
import org.example.domain.employee.EventType
import org.example.domain.employee.RetornoEvent
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.LocalDateTimeSerializer
import org.example.infra.repository.employee.EmployeeRepository
import java.time.LocalDateTime
import java.util.UUID

class Retorno(
    private val employeeRepository: EmployeeRepository,
    private val jwtService: JwtService
) {
    fun execute(employeeId: UUID, contractId: UUID, input: RetornoInput, accessToken: String): RetornoOutput {
        // validate accessToken
        jwtService.isValid(accessToken)
        // check if employee and contract exists
        val employee: Employee = employeeRepository.findById(employeeId)
            ?: throw EmployeeExceptions.NotFound()
        val contract = employee.contracts.singleOrNull { it.id == contractId }
            ?: throw ContractExceptions.ContractNotFound()
        // instantiate a state machine
        val stateMachine = ContractStateMachine(contract)
        // handle event, it changes the contract
        stateMachine.handle(ContractEvent.Retorno)
        // create a new afastamento event
        val event = RetornoEvent(input.reason)
        // add event to events contract
        contract.addEvent(event)
        // persist
        employeeRepository.update(employee)
        // return event data
        return RetornoOutput(
            event.type,
            event.createdAt,
            event.reason
        )
    }
}

@Serializable
data class RetornoInput(
    val reason: String
)

@Serializable
data class RetornoOutput(
    val type: EventType,
    @Serializable(with = LocalDateTimeSerializer::class)
    val entryDate: LocalDateTime,
    val reason: String
)
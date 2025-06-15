package org.example.application.usecase.events

import kotlinx.serialization.Serializable
import org.example.domain.employee.*
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.LocalDateTimeSerializer
import org.example.infra.repository.employee.EmployeeRepository
import java.time.LocalDateTime
import java.util.*

class Afastamento(
    private val employeeRepository: EmployeeRepository,
    private val jwtService: JwtService
) {
    fun execute(employeeId: UUID, contractId: UUID, input: AfastamentoInput, accessToken: String): AfastamentoOutput {
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
        stateMachine.handle(ContractEvent.Afastamento)
        // create a new afastamento event
        val event = AfastamentoEvent(input.reason)
        // add event to events contract
        contract.addEvent(event)
        // persist
        employeeRepository.update(employee)
        // return event data
        return AfastamentoOutput(
            event.type,
            event.createdAt,
            event.reason
        )
    }
}


@Serializable
data class AfastamentoInput(
    val reason: String
)

@Serializable
data class AfastamentoOutput(
    val type: EventType,
    @Serializable(with = LocalDateTimeSerializer::class)
    val entryDate: LocalDateTime,
    val reason: String
)

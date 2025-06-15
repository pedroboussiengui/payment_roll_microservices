package org.example.application.usecase.employee

import kotlinx.serialization.Serializable
import org.example.domain.employee.AdmissionEvent
import org.example.domain.employee.Contract
import org.example.domain.employee.ContractEvent
import org.example.domain.employee.ContractStateMachine
import org.example.domain.employee.ContractType
import org.example.domain.employee.EmployeeExceptions
import org.example.domain.employee.EventType
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.LocalDateTimeSerializer
import org.example.infra.repository.employee.EmployeeRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Admission(
    private val employeeRepository: EmployeeRepository,
    private val jwtService: JwtService
) {
    fun execute(employeeId: UUID, input: AdmissionInput, accessToken: String): AdmissionOutput {
        // validate accessToken
        jwtService.isValid(accessToken)
        // check if employee exists
        val employee = employeeRepository.findById(employeeId)
            ?: throw EmployeeExceptions.NotFound()
        // create a new contract with input data
        val contract = Contract.create(
            matricula = input.matricula,
            entryDate = input.entryDate,
            exerciseDate = input.exerciseDate,
            contractType = input.contractType,
            department = input.department,
            position = input.position,
            function = input.function
        )
        // handle contract in state machine
        val stateMachine = ContractStateMachine(contract)
        stateMachine.handle(ContractEvent.Admission)
        // add contract to employee
        employee.addContract(contract)
        // create admission event
        val event = AdmissionEvent(input.entryDate, input.exerciseDate)
        // add event to events contract
        contract.addEvent(event)
        // persist in database
        employeeRepository.update(employee)
        // return event data
        return AdmissionOutput(
            event.type,
            event.entryDate,
            event.createdAt,
            event.exerciseDate
        )
    }
}

@Serializable
data class AdmissionInput(
    val matricula: String,
    @Serializable(with = LocalDateSerializer::class)
    val entryDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val exerciseDate: LocalDate,
    val contractType: ContractType,
    val department: String,
    val position: String,
    val function: String?
)

@Serializable
data class AdmissionOutput(
    val type: EventType,
    @Serializable(with = LocalDateSerializer::class)
    val entryDate: LocalDate,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Serializable(with = LocalDateSerializer::class)
    val exerciseDate: LocalDate
)
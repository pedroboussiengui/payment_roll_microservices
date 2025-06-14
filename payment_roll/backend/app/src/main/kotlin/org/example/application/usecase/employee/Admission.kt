package org.example.application.usecase.employee

import kotlinx.serialization.Serializable
import org.example.domain.employee.AdmissionEvent
import org.example.domain.employee.Contract
import org.example.domain.employee.ContractType
import org.example.domain.employee.EmployeeExceptions
import org.example.domain.employee.Event
import org.example.domain.employee.EventType
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.LocalDateTimeSerializer
import org.example.infra.repository.employee.EmployeeRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Admission(
    private val employeeRepository: EmployeeRepository
) {
    fun execute(employeeId: UUID, input: AdmissionInput): AdmissionOutput {
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
        // add contract to employee
        employee.addContract(contract)
        // create admission event
        val admissionEvent = AdmissionEvent(input.entryDate, input.exerciseDate)
        // add event to events contract
        contract.addEvent(admissionEvent)
        // persist
        employeeRepository.update(employee)
        // return event data
        return AdmissionOutput(
            admissionEvent.type,
            admissionEvent.entryDate,
            admissionEvent.createdAt,
            admissionEvent.exerciseDate
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
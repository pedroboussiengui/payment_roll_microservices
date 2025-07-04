package org.example.application.usecase.employee

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.domain.employee.AdmissionEvent
import org.example.domain.employee.AfastamentoEvent
import org.example.domain.employee.ContractExceptions
import org.example.domain.employee.Employee
import org.example.domain.employee.EmployeeExceptions
import org.example.domain.employee.EventType
import org.example.domain.employee.RetornoEvent
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.LocalDateTimeSerializer
import org.example.infra.repository.employee.EmployeeRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class ListContractEvents(
    private val employeeRepository: EmployeeRepository,
    private val jwtService: JwtService
) {
    fun execute(employeeId: UUID, contractId: UUID, accessToken: String): List<EventOutputDTO> {
        // validate accessToken
        jwtService.isValid(accessToken)
        val employee: Employee = employeeRepository.findById(employeeId)
            ?: throw EmployeeExceptions.NotFound()
        val contract = employee.contracts.singleOrNull { it.id == contractId }
            ?: throw ContractExceptions.ContractNotFound()

        val output = mutableListOf<EventOutputDTO>()
        contract.events.map { event ->
            when (event) {
                is AdmissionEvent -> AdmissionEventOutputDTO(
                    entryDate = event.entryDate,
                    exerciseDate = event.exerciseDate,
                    createdAt = event.createdAt
                ).also { output.add(it) }
                is AfastamentoEvent -> AfastamentoEventOutputDTO(
                    reason = event.reason,
                    createdAt = event.createdAt
                ).also { output.add(it) }
                is RetornoEvent -> RetornoEventOutput(
                    reason = event.reason,
                    createdAt = event.createdAt
                ).also { output.add(it) }
            }
        }
        output.sortByDescending { it.createdAt }
        return output
    }
}

@Serializable
sealed interface EventOutputDTO {
    val type: EventType
    val createdAt: LocalDateTime
}

@Serializable
data class AdmissionEventOutputDTO(
    @Serializable(with = LocalDateSerializer::class)
    val entryDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val exerciseDate: LocalDate,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdAt: LocalDateTime,
    @SerialName("eventType")
    override val type: EventType = EventType.Admission
) : EventOutputDTO

@Serializable
data class AfastamentoEventOutputDTO(
    val reason: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdAt: LocalDateTime,
    @SerialName("eventType")
    override val type: EventType = EventType.Afastamento
) : EventOutputDTO

@Serializable
data class RetornoEventOutput(
    val reason: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    override val createdAt: LocalDateTime,
    @SerialName("eventType")
    override val type: EventType = EventType.Retorno
) : EventOutputDTO
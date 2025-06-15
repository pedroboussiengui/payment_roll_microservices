package org.example.application.usecase.employee

import kotlinx.serialization.Serializable
import org.example.domain.employee.ContractExceptions
import org.example.domain.employee.ContractType
import org.example.domain.employee.Employee
import org.example.domain.employee.EmployeeExceptions
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.UUIDSerializer
import org.example.infra.repository.employee.EmployeeRepository
import java.time.LocalDate
import java.util.*

class DetailEmployeeContract(
    private val employeeRepository: EmployeeRepository,
    private val jwtService: JwtService
) {
    fun execute(employeeId: UUID, contractId: UUID, accessToken: String): EmployeeContractFullOutput {
        val employee: Employee = employeeRepository.findById(employeeId)
            ?: throw EmployeeExceptions.NotFound()
        val contract = employee.contracts.singleOrNull { it.id == contractId }
            ?: throw ContractExceptions.ContractNotFound()

        return EmployeeContractFullOutput(
            id = contract.id!!,
            matricula = contract.matricula,
            entryDate = contract.entryDate,
            contractType = contract.contractType,
            contractState = contract.contractState.toString(),
            position = contract.position,
            function = contract.function,
            department = contract.department,
            possibleEvents = contract.possibleEvents.map { it.toString() }
        )
    }
}

@Serializable
data class EmployeeContractFullOutput(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val matricula: String,
    @Serializable(with = LocalDateSerializer::class)
    val entryDate: LocalDate,
    val contractType: ContractType,
    val contractState: String,
    val position: String,
    val function: String?,
    val department: String,
    val possibleEvents: List<String>
)

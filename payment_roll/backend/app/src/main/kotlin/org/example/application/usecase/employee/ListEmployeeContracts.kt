package org.example.application.usecase.employee

import kotlinx.serialization.Serializable
import org.example.domain.employee.ContractState
import org.example.domain.employee.ContractType
import org.example.domain.employee.Employee
import org.example.domain.employee.EmployeeExceptions
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.UUIDSerializer
import org.example.infra.repository.employee.EmployeeRepository
import java.time.LocalDate
import java.util.*

class ListEmployeeContracts(
    private val employeeRepository: EmployeeRepository
) {
    fun execute(employeeId: UUID): List<EmployeeContractOutput> {
        val employee: Employee = employeeRepository.findById(employeeId)
            ?: throw EmployeeExceptions.NotFound()

        val output = mutableListOf<EmployeeContractOutput>()
        employee.contracts.map { contract ->
            EmployeeContractOutput(
                id = contract.id!!,
                matricula = contract.matricula,
                entryDate = contract.entryDate,
                contractType = contract.contractType,
                contractState = contract.contractState.toString(),
                position = contract.position,
                function = contract.function,
                department = contract.department
            ).also { output.add(it) }
        }
        return output
    }
}

@Serializable
data class EmployeeContractOutput(
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
)
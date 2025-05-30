package org.example

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

class ListEmployeeContracts(
    private val employeeDao: EmployeeDao
) {
    fun execute(employeeId: String): List<EmployeeContractOutput> {
        val employee: Employee = employeeDao.findById(employeeId)
            ?: throw Exception("Employee not found")

        val output = mutableListOf<EmployeeContractOutput>()
        employee.contracts.map { contract ->
            EmployeeContractOutput(
                id = contract.id,
                matricula = contract.matricula,
                entryDate = contract.entryDate,
                contractType = contract.contractType,
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
    val position: String,
    val function: String?,
    val department: String,
)
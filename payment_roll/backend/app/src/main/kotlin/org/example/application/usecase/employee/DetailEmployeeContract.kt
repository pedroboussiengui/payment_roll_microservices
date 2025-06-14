package org.example.application.usecase.employee

import org.example.domain.employee.ContractExceptions
import org.example.domain.employee.Employee
import org.example.domain.employee.EmployeeExceptions
import org.example.infra.jwt.JwtService
import org.example.infra.repository.employee.EmployeeRepository
import java.util.*

class DetailEmployeeContract(
    private val employeeRepository: EmployeeRepository,
    private val jwtService: JwtService
) {
    fun execute(employeeId: UUID, contractId: UUID, accessToken: String): EmployeeContractOutput {
        val employee: Employee = employeeRepository.findById(employeeId)
            ?: throw EmployeeExceptions.NotFound()
        val contract = employee.contracts.singleOrNull { it.id == contractId }
            ?: throw ContractExceptions.ContractNotFound()

        return EmployeeContractOutput(
            id = contract.id!!,
            matricula = contract.matricula,
            entryDate = contract.entryDate,
            contractType = contract.contractType,
            contractState = contract.contractState.toString(),
            position = contract.position,
            function = contract.function,
            department = contract.department
        )
    }
}


package org.example.application.usecase

import kotlinx.serialization.Serializable
import org.example.domain.employee.Employee
import org.example.domain.employee.EmployeeExceptions
import org.example.infra.jwt.JwtService
import org.example.infra.repository.EmployeeDao
import org.example.infra.ktor.UUIDSerializer
import java.util.UUID

class AddEmployee(
    private val employeeDao: EmployeeDao,
    private val jwtService: JwtService
) {
    fun execute(input: AddEmployeeInput, accessToken: String): AddEmployeeOutput {
//        jwtService.isValid(accessToken)
        employeeDao.findByDocument(input.document)?.let {
            throw EmployeeExceptions.UnicityViolation("Employee with given document already exists")
        }
        val employee = Employee.create(
            input.name,
            input.document,
            input.birthDate)
        val validationResult = employee.validate()
        if (validationResult.isNotEmpty()) {
            throw EmployeeExceptions.BusinessRuleViolation(
                "Violations fo business rules was found in input data",
                validationResult
            )
        }
        employeeDao.add(employee)
        return AddEmployeeOutput(employee.id)
    }
}

@Serializable
data class AddEmployeeInput(
    val name: String,
    val document: String,
    val birthDate: String,
)

@Serializable
data class AddEmployeeOutput(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID
)
package org.example.application.usecase.employee

import kotlinx.serialization.Serializable
import org.example.domain.employee.Employee
import org.example.domain.employee.EmployeeExceptions
import org.example.domain.employee.Gender
import org.example.domain.employee.MaritalStatus
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.UUIDSerializer
import org.example.infra.repository.employee.EmployeeRepository
import java.time.LocalDate
import java.util.*

class AddEmployee(
    private val employeeRepository: EmployeeRepository,
    private val jwtService: JwtService
) {
    fun execute(input: AddEmployeeInput, accessToken: String): AddEmployeeOutput {
        jwtService.isValid(accessToken)
        employeeRepository.findByDocument(input.document)?.let {
            throw EmployeeExceptions.UnicityViolation("Employee with given document already exists")
        }
        val employee = Employee.create(
            input.name,
            input.document,
            input.birthDate,
            input.identity,
            input.maritalStatus,
            input.gender,
            input.motherName,
            input.fatherName
        )
        val validationResult = employee.validate()
        if (validationResult.isNotEmpty()) {
            throw EmployeeExceptions.BusinessRuleViolation(
                "Violations fo business rules was found in input data",
                validationResult
            )
        }
//        employeeDao.add(employee)
//        return AddEmployeeOutput(employee.id)
        val generatedId = employeeRepository.add(employee)
        return AddEmployeeOutput(generatedId)
    }
}

@Serializable
data class AddEmployeeInput(
    val name: String,
    val document: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val identity: String,
    val maritalStatus: MaritalStatus,
    val gender: Gender,
    val motherName: String,
    val fatherName: String?
)

@Serializable
data class AddEmployeeOutput(
    @Serializable(with = UUIDSerializer::class)
    val employeeId: UUID
)
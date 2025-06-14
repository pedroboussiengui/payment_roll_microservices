package org.example.application.usecase.employee

import kotlinx.serialization.Serializable
import org.example.domain.employee.Gender
import org.example.domain.employee.MaritalStatus
import org.example.infra.jwt.JwtService
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.UUIDSerializer
import org.example.infra.repository.employee.EmployeeRepository
import java.time.LocalDate
import java.util.*

class ListEmployees(
    private val employeeRepository: EmployeeRepository,
    private val jwtService: JwtService
) {
    fun execute(accessToken: String): List<ListEmployeeByIDOutput> {
//        jwtService.isValid(accessToken)
        val output = mutableListOf<ListEmployeeByIDOutput>()
        employeeRepository.findAll().map { employee ->
            output.add(
                ListEmployeeByIDOutput(
                    id = employee.id!!,
                    name = employee.name,
                    document = employee.document,
                    birthDate = employee.birthDate,
                    identity = employee.identity,
                    maritalStatus = employee.maritalStatus,
                    gender = employee.gender,
                    motherName = employee.motherName,
                    fatherName = employee.fatherName
                )
            )
        }
        return output
    }
}

class AuthenticationFailedException(message: String) : RuntimeException(message)

@Serializable
data class ListEmployeeByIDOutput(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
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
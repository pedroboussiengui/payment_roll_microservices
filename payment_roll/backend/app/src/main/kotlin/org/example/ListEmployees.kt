package org.example

import kotlinx.serialization.Serializable
import org.example.jwt.JwtService
import java.time.LocalDate
import java.util.UUID

class ListEmployees(
    private val employeeDao: EmployeeDao
) {
    private val jwtService = JwtService()
    fun execute(accessToken: String): List<ListEmployeeByIDOutput> {
        if (!jwtService.isValid(accessToken)) {
            throw AuthenticationFailedException("Invalid token")
        }
        val output = mutableListOf<ListEmployeeByIDOutput>()
        employeeDao.findAll().map { employee ->
            output.add(
                ListEmployeeByIDOutput(
                    id = employee.id,
                    name = employee.name,
                    document = employee.document,
                    birthDate = employee.birthDate
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
    val birthDate: LocalDate
)
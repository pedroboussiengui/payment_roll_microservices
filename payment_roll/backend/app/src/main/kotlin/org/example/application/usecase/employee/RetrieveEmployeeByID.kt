package org.example.application.usecase.employee

import kotlinx.serialization.Serializable
import org.example.domain.employee.Employee
import org.example.domain.employee.EmployeeExceptions
import org.example.domain.employee.Gender
import org.example.domain.employee.MaritalStatus
import org.example.infra.jwt.JwtService
import org.example.infra.repository.EmployeeDao
import org.example.infra.ktor.LocalDateSerializer
import org.example.infra.ktor.UUIDSerializer
import java.time.LocalDate
import java.util.UUID

class RetrieveEmployeeByID(
    private val employeeDao: EmployeeDao,
    private val jwtService: JwtService
) {
    fun execute(employeeId: String, accessToken: String): RetrieveEmployeeByIDOutput {
//        jwtService.isValid(accessToken)
        val employee: Employee = employeeDao.findById(employeeId)
            ?: throw EmployeeExceptions.NotFound()

        return RetrieveEmployeeByIDOutput(
            id = employee.id,
            name = employee.name,
            document = employee.document,
            birthDate = employee.birthDate,
            identity = employee.identity,
            maritalStatus = employee.maritalStatus,
            gender = employee.gender,
            motherName = employee.motherName,
            fatherName = employee.fatherName
        )
    }
}

@Serializable
data class RetrieveEmployeeByIDOutput(
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
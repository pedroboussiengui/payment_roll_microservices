package org.example

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

class RetrieveEmployeeByID(
    private val employeeDao: EmployeeDao
) {
    fun execute(employeeId: String): RetrieveEmployeeByIDOutput {
        val employee: Employee = employeeDao.findById(employeeId)
            ?: throw Exception("Employee not found")

        return RetrieveEmployeeByIDOutput(
            id = employee.id,
            name = employee.name,
            document = employee.document,
            birthDate = employee.birthDate
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
    val birthDate: LocalDate
)
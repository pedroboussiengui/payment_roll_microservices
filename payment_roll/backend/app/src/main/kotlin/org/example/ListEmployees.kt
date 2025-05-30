package org.example

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

class ListEmployees(
    private val employeeDao: EmployeeDao
) {
    fun execute(): List<ListEmployeeByIDOutput> {
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


@Serializable
data class ListEmployeeByIDOutput(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val document: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate
)
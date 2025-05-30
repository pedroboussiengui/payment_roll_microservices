package org.example

import kotlinx.serialization.Serializable
import java.util.UUID

class AddEmployee(
    private val employeeDao: EmployeeDao
) {
    fun execute(input: AddEmployeeInput): AddEmployeeOutput {
        val employee = Employee.create(
            input.id,
            input.name,
            input.document,
            input.birthDate)
        employeeDao.add(employee)
        return AddEmployeeOutput(employee.id)
    }
}

@Serializable
data class AddEmployeeInput(
    val id: String,
    val name: String,
    val document: String,
    val birthDate: String,
)
@Serializable
data class AddEmployeeOutput(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID
)
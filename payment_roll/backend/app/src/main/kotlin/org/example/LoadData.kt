package org.example

import org.example.domain.employee.Contract
import org.example.domain.employee.ContractType
import org.example.domain.employee.Employee
import org.example.domain.employee.Gender
import org.example.domain.employee.MaritalStatus
import org.example.infra.repository.EmployeeDao
import java.time.LocalDate
import java.util.UUID

class LoadData(employeeDao: EmployeeDao) {

    init {
        val employee = Employee(
            UUID.fromString("805a852d-61e8-4a07-9e1f-02141ae74e94"),
            "John Doe",
            "123.456.789-00",
            LocalDate.of(1990, 1, 1),
            "123456789",
            MaritalStatus.single,
            Gender.male,
            "Mary Doe",
            "Peter Doe"
        )
        employee.addContract(
            Contract(
                UUID.fromString("0042d963-6c54-4c9f-a60c-bfcde866d29e"),
                "2025001",
                LocalDate.of(2025, 1, 1),
                ContractType.celetista,
                "Software Engineer",
                "Backend Developer",
                "IT"
            )
        )
        employee.addContract(
            Contract(
                UUID.fromString("97c4b37c-b652-42f5-836e-e279f189e802"),
                "2025002",
                LocalDate.of(2024, 1, 1),
                ContractType.temporario,
                "Project Manager",
                null,
                "Management"
            )
        )
        employeeDao.add(employee)

        employeeDao.add(
            Employee(
                UUID.fromString("f2d3b4c5-6a7b-8c9d-0e1f-2a3b4c5d6e7f"),
                "Jane Smith",
                "987.654.321-00",
                LocalDate.of(1995, 5, 15),
                "987654321",
                MaritalStatus.married,
                Gender.female,
                "Alice Smith",
                "Bob Smith"
            )
        )

        employeeDao.add(
            Employee(
                UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d"),
                "Peter Jones",
                "111.222.333-44",
                LocalDate.of(1988, 11, 30),
                "111222333",
                MaritalStatus.divorced,
                Gender.male,
                "Nancy Jones",
                "David Jones"
            )
        )

        employeeDao.add(
            Employee(
                UUID.fromString("f9e8d7c6-b5a4-3b2c-1d0e-f9a8b7c6d5e4"),
                "Mary Brown",
                "555.666.777-88",
                LocalDate.of(2000, 7, 22),
                "555666777",
                MaritalStatus.widowed,
                Gender.female,
                "Susan Brown",
                null
            )
        )
    }

}
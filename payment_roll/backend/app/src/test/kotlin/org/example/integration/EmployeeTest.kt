package org.example.integration

import org.example.application.usecase.employee.AddEmployee
import org.example.application.usecase.employee.AddEmployeeInput
import org.example.application.usecase.employee.ListEmployeeContracts
import org.example.application.usecase.employee.ListEmployees
import org.example.application.usecase.employee.RetrieveEmployeeByID
import org.example.domain.employee.EmployeeExceptions
import org.example.domain.employee.Gender
import org.example.domain.employee.MaritalStatus
import org.example.infra.jwt.Auth0JwtService
import org.example.infra.repository.employee.EmployeeRepositoryImpl
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.sql.Connection
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class EmployeeTest {

    lateinit var addEmployee: AddEmployee
    lateinit var retrieveEmployeeByID: RetrieveEmployeeByID
    lateinit var listEmployeeContracts: ListEmployeeContracts
    lateinit var listEmployees: ListEmployees

    @BeforeEach
    fun setUp() {
        Database.Companion.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=1", driver = "org.h2.Driver")
        TransactionManager.Companion.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        val employeeRepository = EmployeeRepositoryImpl()
        val jwtService = Auth0JwtService()
        listEmployeeContracts = ListEmployeeContracts(employeeRepository)
        retrieveEmployeeByID = RetrieveEmployeeByID(employeeRepository, jwtService)
        listEmployees = ListEmployees(employeeRepository, jwtService)
        addEmployee = AddEmployee(employeeRepository, jwtService)
    }

    @Test
    fun `should add a new employee`() {
        val input = AddEmployeeInput(
            "John Doe",
            "123.456.789-00",
            LocalDate.of(1990, 1, 1),
            "123456789",
            MaritalStatus.single,
            Gender.male,
            "Mary Doe",
            "Peter Doe"
        )
        val output = addEmployee.execute(input, "")
        assertNotNull(output.employeeId)

        val output2 = retrieveEmployeeByID.execute(output.employeeId, "")
        assertAll(
            { assertEquals(input.name, output2.name) },
            { assertEquals(input.document, output2.document) },
            { assertEquals(input.birthDate, output2.birthDate) },
            { assertEquals(input.identity, output2.identity) },
            { assertEquals(input.maritalStatus, output2.maritalStatus) },
            { assertEquals(input.gender, output2.gender) },
            { assertEquals(input.motherName, output2.motherName) },
            { assertEquals(input.fatherName, output2.fatherName) }
        )
    }

    @Test
    fun `should add a new employee and validate its contracts`() {
        val input = AddEmployeeInput(
            "John Doe",
            "123.456.789-00",
            LocalDate.of(1990, 1, 1),
            "123456789",
            MaritalStatus.single,
            Gender.male,
            "Mary Doe",
            "Peter Doe"
        )
        val output = addEmployee.execute(input, "")
        val output2 = listEmployeeContracts.execute(output.employeeId)
        assertNotNull(output2.size)
        assertEquals(0, output2.size)
    }

    @Test
    fun `should add a multiples employees`() {
        val input = AddEmployeeInput(
            "John Doe",
            "123.456.789-00",
            LocalDate.of(1990, 1, 1),
            "123456789",
            MaritalStatus.single,
            Gender.male,
            "Mary Doe",
            "Peter Doe"
        )
        addEmployee.execute(input, "")
        val input2 = AddEmployeeInput(
            "Jane Smith",
            "987.654.321-00",
            LocalDate.of(1995, 5, 15),
            "987654321",
            MaritalStatus.married,
            Gender.female,
            "Alice Smith",
            "Bob Smith"
        )
        addEmployee.execute(input2, "")

        val output = listEmployees.execute("")
        assertEquals(2, output.size)
    }

    @Test
    fun `should throw an exception when add employee with invalid data`() {
        val input = AddEmployeeInput(
            "", // Invalid name
            "12345678900", // invalid document
            LocalDate.of(1990, 1, 1),
            "123456789",
            MaritalStatus.single,
            Gender.male,
            "Mary Doe",
            "Peter Doe"
        )
        assertFailsWith<EmployeeExceptions.BusinessRuleViolation> {
            addEmployee.execute(input, "")
        }
    }
}
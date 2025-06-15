package org.example.integration

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.example.application.usecase.employee.AddEmployee
import org.example.application.usecase.employee.AddEmployeeInput
import org.example.application.usecase.employee.Admission
import org.example.application.usecase.employee.AdmissionInput
import org.example.application.usecase.employee.DetailEmployeeContract
import org.example.application.usecase.employee.ListContractEvents
import org.example.application.usecase.employee.ListEmployeeContracts
import org.example.application.usecase.events.Afastamento
import org.example.application.usecase.events.AfastamentoInput
import org.example.domain.employee.ContractType
import org.example.domain.employee.EventType
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

class AdmissionTest {

    lateinit var addEmployee: AddEmployee
    lateinit var admission: Admission
    lateinit var listEmployeeContracts: ListEmployeeContracts
    lateinit var listContractEvents: ListContractEvents
    lateinit var detailEmployeeContract: DetailEmployeeContract
    lateinit var afastamento: Afastamento

    @BeforeEach
    fun setUp() {
        Database.Companion.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=1", driver = "org.h2.Driver")
        TransactionManager.Companion.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        val employeeRepository = EmployeeRepositoryImpl()
        val jwtService = Auth0JwtService()
        addEmployee = AddEmployee(employeeRepository, jwtService)
        admission = Admission(employeeRepository)
        listEmployeeContracts = ListEmployeeContracts(employeeRepository)
        listContractEvents = ListContractEvents(employeeRepository)
        detailEmployeeContract = DetailEmployeeContract(employeeRepository, jwtService)
        afastamento = Afastamento(employeeRepository)
    }

    @Test
    fun `should perform an admission to create a new contract for employee`() = runTest {
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

        val input2 = AdmissionInput(
            matricula = "2025001",
            entryDate = LocalDate.of(2025, 1, 1),
            exerciseDate = LocalDate.of(2025, 1, 1),
            contractType = ContractType.celetista,
            department = "IT",
            position = "Software Engineer",
            function = "Backend Developer"
        )

        val admissionOutput = admission.execute(output.employeeId, input2)

        val output2 = listEmployeeContracts.execute(output.employeeId)
        assertEquals(1, output2.size)
        assertAll(
            { assertEquals(input2.matricula, output2[0].matricula) },
            { assertEquals(input2.entryDate, output2[0].entryDate) },
            { assertEquals(input2.contractType, output2[0].contractType) },
            { assertEquals(input2.department, output2[0].department) },
            { assertEquals(input2.position, output2[0].position) },
            { assertEquals(input2.function, output2[0].function) }
        )

        val output3 = listContractEvents.execute(output.employeeId, output2[0].id)
        assertEquals(1, output3.size)
        assertEquals(EventType.Admission, output3[0].type)

        val detail1 = detailEmployeeContract.execute(output.employeeId, output2[0].id, "")
        println(detail1)

        val input3 = AfastamentoInput(reason = "Doença")

        afastamento.execute(output.employeeId, output2[0].id, input3)

        val output4 = listContractEvents.execute(output.employeeId, output2[0].id)
        assertEquals(2, output4.size)
        assertEquals(EventType.Admission, output4[0].type)
        assertEquals(EventType.Afastamento, output4[1].type)

        val detail2 = detailEmployeeContract.execute(output.employeeId, output2[0].id, "")
        println(detail2)

        output4.forEach {
            println(it)
        }
    }
}
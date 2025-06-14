package org.example.unit

import org.example.domain.employee.Contract
import org.example.domain.employee.ContractEvent
import org.example.domain.employee.ContractState
import org.example.domain.employee.ContractStateMachine
import org.example.domain.employee.ContractType
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class ContractStateMachineTest {

    @Test
    fun `should handle event and go to correct state in contract state machine`() {
        val contract = Contract.create(
            matricula = "2025001",
            entryDate = LocalDate.of(2025, 1, 1),
            exerciseDate = LocalDate.of(2025, 1, 1),
            contractType = ContractType.celetista,
            department = "IT",
            position = "Software Engineer",
            function = "Backend Developer"
        )
        assertEquals(ContractState.Active, contract.contractState)

        val stateMachine = ContractStateMachine(contract)

        stateMachine.handle(ContractEvent.Afastamento)
        assertEquals(ContractState.Afastado, contract.contractState)
        assertEquals(listOf(ContractEvent.Retorno),contract.possibleEvents)

        stateMachine.handle(ContractEvent.Retorno)
        assertEquals(ContractState.Active, contract.contractState)
        assertEquals(listOf(ContractEvent.Afastamento),contract.possibleEvents)
    }
}
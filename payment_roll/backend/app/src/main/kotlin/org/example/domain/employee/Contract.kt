package org.example.domain.employee

import org.example.domain.employee.ContractState.Active
import org.example.domain.employee.ContractState.Afastado
import java.time.LocalDate
import java.util.*

enum class ContractType { celetista, estagiario, temporario }

data class Contract(
    val id: UUID?,
    val matricula: String,
    val entryDate: LocalDate,
    val exerciseDate: LocalDate,
    val contractType: ContractType,
    var contractState: ContractState,
    val department: String,
    val position: String,
    val function: String?,
    val events: MutableList<Event> = mutableListOf()
) {
    companion object {
        fun create(
            matricula: String,
            entryDate: LocalDate,
            exerciseDate: LocalDate,
            contractType: ContractType,
            department: String,
            position: String,
            function: String?
        ): Contract {
//            val contractId = UUID.randomUUID()
            return Contract(
                null,
                matricula,
                entryDate,
                exerciseDate,
                contractType,
                ContractState.Inactive,
                department,
                position,
                function
            )
        }
    }

    fun addEvent(event: Event) {
        events.add(event)
    }

    var possibleEvents: List<ContractEvent> = mutableListOf()

    fun admit() {
        contractState = Active
        possibleEvents = listOf(
            ContractEvent.Afastamento
        )
    }

    fun afastar() {
        contractState = Afastado
        possibleEvents = listOf(
            ContractEvent.Retorno
        )
    }

    fun remove() {
        contractState = Active
        possibleEvents = listOf(
            ContractEvent.Afastamento
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Contract) return false

        return matricula == other.matricula &&
                entryDate == other.entryDate &&
                exerciseDate == other.exerciseDate &&
                contractType == other.contractType &&
                department == other.department &&
                position == other.position &&
                function == other.function
    }

    override fun hashCode(): Int {
        return listOf(
            matricula, entryDate, exerciseDate,
            contractType, department, position, function,
            contractState
        ).hashCode()
    }
}
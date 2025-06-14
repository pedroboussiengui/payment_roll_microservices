package org.example.domain.employee

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
    val events: MutableList<Event> = mutableListOf(),
    private val _pendingEvents: MutableList<Event> = mutableListOf()
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
                ContractState.Active,
                department,
                position,
                function
            )
        }
    }

    fun addEvent(event: Event) {
        events.add(event)
        _pendingEvents.add(event)
    }

    fun setState(state: ContractState) {
        contractState = state
    }

    var possibleEvents: List<ContractEvent> = mutableListOf()

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
            contractType, department, position, function
        ).hashCode()
    }

    fun collectNewEvents(): List<Event> {
        val toInsert = _pendingEvents.toList()
        _pendingEvents.clear()
        return toInsert
    }
}
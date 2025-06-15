package org.example.domain.employee

sealed class ContractState {

    abstract fun updateContract(contract: Contract)

    object Inactive : ContractState() {
        override fun updateContract(contract: Contract) {
            contract.possibleEvents = listOf(
                ContractEvent.Admission
            )
        }
    }

    object Active : ContractState() {
        override fun updateContract(contract: Contract) {
            contract.contractState = Active
            contract.possibleEvents = listOf(
                ContractEvent.Afastamento
            )
        }
    }

    object Afastado : ContractState() {
        override fun updateContract(contract: Contract) {
            contract.contractState = Afastado
            contract.possibleEvents = listOf(
                ContractEvent.Retorno
            )
        }
    }

    override fun toString(): String = when(this) {
        Inactive -> "Inactive"
        Active -> "Active"
        Afastado -> "Afastado"
    }

    companion object {
        fun fromString(state: String): ContractState = when(state) {
            "Inactive" -> Inactive
            "Active" -> Active
            "Afastado" -> Afastado
            else -> throw IllegalArgumentException("Unknown contract state: $state")
        }
    }
}

sealed class ContractEvent {
    object Admission : ContractEvent()
    object Afastamento : ContractEvent()
    object Retorno : ContractEvent()

    override fun toString(): String = when(this) {
        Admission -> "Admission"
        Afastamento -> "Afastamento"
        Retorno -> "Retorno"
    }

    companion object {
        fun fromString(event: String): ContractEvent = when(event) {
            "Admission" -> Admission
            "Afastamento" -> Afastamento
            "Retorno" -> Retorno
            else -> throw IllegalArgumentException("Unknown contract event: $event")
        }
    }
}

val transitionTable: Map<Pair<ContractState, ContractEvent>, ContractState> = mapOf(
    (ContractState.Inactive to ContractEvent.Admission) to ContractState.Active,
    (ContractState.Active to ContractEvent.Afastamento) to ContractState.Afastado,
    (ContractState.Afastado to ContractEvent.Retorno) to ContractState.Active
)

class ContractStateMachine(val contract: Contract) {
    val currentState = contract.contractState

    fun handle(event: ContractEvent) {
        val newState = transitionTable[currentState to event]
            ?: throw ContractExceptions.UnableHandleEvent(
                "Invalid operation: contract cannot handle $event when it state is $currentState"
            )
        newState.updateContract(contract)
    }
}
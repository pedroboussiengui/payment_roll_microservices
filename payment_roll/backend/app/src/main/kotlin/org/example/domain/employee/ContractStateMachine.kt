package org.example.domain.employee

sealed class ContractState {
    abstract fun possibleEvents(): List<ContractEvent>

    object Active : ContractState() {
        override fun possibleEvents(): List<ContractEvent> {
            return listOf(
                ContractEvent.Afastamento
            )
        }
    }

    object Afastado : ContractState() {
        override fun possibleEvents(): List<ContractEvent> {
            return listOf(
                ContractEvent.Retorno
            )
        }
    }

    override fun toString(): String = when(this) {
        Active -> "Active"
        Afastado -> "Afastado"
    }

    companion object {
        fun fromString(state: String): ContractState = when(state) {
            "Active" -> Active
            "Afastado" -> Afastado
            else -> throw IllegalArgumentException("Unknown contract state: $state")
        }
    }
}

sealed class ContractEvent {
    object Afastamento : ContractEvent()
    object Retorno : ContractEvent()
}

val transitionTable: Map<Pair<ContractState, ContractEvent>, ContractState> = mapOf(
    (ContractState.Active to ContractEvent.Afastamento) to ContractState.Afastado,
    (ContractState.Afastado to ContractEvent.Retorno) to ContractState.Active
)

class ContractStateMachine(val contract: Contract) {

    fun handle(event: ContractEvent) {
        val transition = transitionTable[contract.contractState to event]
            ?: throw IllegalArgumentException("Invalid transition")
        contract.setState(transition)
        contract.possibleEvents = transition.possibleEvents()
    }
}
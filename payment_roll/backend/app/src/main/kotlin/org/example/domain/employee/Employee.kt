package org.example.domain.employee

import java.time.LocalDate
import java.util.UUID

data class Employee(
    val id: UUID,
    val name: String,
    val document: String,
    val birthDate: LocalDate,
    val contracts: MutableList<Contract> = mutableListOf()
) {
    companion object {
        fun create(
            id: String,
            name: String,
            document: String,
            birthDate: String
        ): Employee {
            val userId = UUID.fromString(id)
            val birthDate = LocalDate.parse(birthDate)
            return Employee(
                userId,
                name,
                document,
                birthDate
            )
        }
    }

    fun addContract(contract: Contract) {
        contracts.add(contract)
    }
}
package org.example.domain.employee

import java.time.LocalDate
import java.util.UUID

enum class MaritalStatus {
    single,
    married,
    divorced,
    widowed
}

enum class Gender {
    male,
    female
}

data class Employee(
    val id: UUID?,
    val name: String,
    val document: String,
    val birthDate: LocalDate,
    val identity: String,
    val maritalStatus: MaritalStatus,
    val gender: Gender,
    val motherName: String,
    val fatherName: String?,
    val contracts: MutableList<Contract> = mutableListOf()
) {
    companion object {
        fun create(
            id: String,
            name: String,
            document: String,
            birthDate: LocalDate,
            identity: String,
            maritalStatus: MaritalStatus,
            gender: Gender,
            motherName: String,
            fatherName: String?
        ): Employee {
            val userId = UUID.fromString(id)
            return Employee(
                userId,
                name,
                document,
                birthDate,
                identity,
                maritalStatus,
                gender,
                motherName,
                fatherName
            )
        }

        fun create(
            name: String,
            document: String,
            birthDate: LocalDate,
            identity: String,
            maritalStatus: MaritalStatus,
            gender: Gender,
            motherName: String,
            fatherName: String?
        ): Employee {
//            val userId = UUID.randomUUID()
            return Employee(
                null,
                name,
                document,
                birthDate,
                identity,
                maritalStatus,
                gender,
                motherName,
                fatherName
            )
        }
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (name.isBlank()) {
            errors.add("Name cannot be null or blank")
        }
        if (!document.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$".toRegex())) {
            errors.add("Invalid document format, should be XXX.XXX.XXX-XX")
        }
        if (birthDate.isAfter(LocalDate.now())) {
            errors.add("Birth date cannot be in the future")
        }
        return errors
    }

    fun addContract(contract: Contract) {
        contracts.add(contract)
    }
}
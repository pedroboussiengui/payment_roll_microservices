package org.example.domain.organization

import java.time.LocalDate
import java.util.UUID

class Organization(
    val id: UUID,
    val name: String,
    val cnpj: String,
    val sigla: String,
    val parentId: UUID? = null,
    val createdAt: LocalDate
) {
    companion object {
        fun create(name: String, cnpj: String, sigla: String, parentId: UUID?): Organization {
            return Organization(
                id = UUID.randomUUID(),
                name = name,
                cnpj = cnpj,
                sigla = sigla,
                parentId = parentId,
                createdAt = LocalDate.now()
            )
        }
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (name.isBlank()) {
            errors.add("Organization name cannot be null or blank")
        }
        if (!cnpj.matches("^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$".toRegex())) {
            errors.add("Invalid cnpj format, should be XXX.XXX.XXX-XX")
        }
        if (sigla.isBlank()) {
            errors.add("Organization sigla cannot be null or black")
        }
        return errors
    }
}
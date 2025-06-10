package org.example.domain.employee

import java.time.LocalDate
import java.util.*

data class Contract(
    val id: UUID,
    val matricula: String,
    val entryDate: LocalDate,
    val contractType: ContractType,
    val position: String,
    val function: String?,
    val department: String,
)

enum class ContractType { celetista, estagiario, temporario }

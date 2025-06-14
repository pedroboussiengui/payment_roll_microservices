package org.example.domain.employee

import java.time.LocalDate
import java.time.LocalDateTime

enum class EventType {
    Admission,
    Afastamento
}

sealed class Event {
    abstract val type: EventType
    abstract val createdAt: LocalDateTime
}

data class AdmissionEvent(
    val entryDate: LocalDate,
    val exerciseDate: LocalDate,
    override val createdAt: LocalDateTime = LocalDateTime.now()
) : Event() {
    override val type: EventType = EventType.Admission
}

data class AfastamentoEvent(
    val reason: String,
    override val createdAt: LocalDateTime = LocalDateTime.now()
) : Event() {
    override val type: EventType = EventType.Afastamento
}

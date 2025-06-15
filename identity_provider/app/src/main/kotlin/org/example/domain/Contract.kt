package org.example.domain

import java.util.UUID

class Contract(
    val id: UUID,
    val matricula: String,
    val organization: String,
    val department: String
)
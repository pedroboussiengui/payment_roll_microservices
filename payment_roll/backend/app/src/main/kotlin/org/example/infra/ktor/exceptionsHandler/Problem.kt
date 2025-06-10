package org.example.infra.ktor.exceptionsHandler

import kotlinx.serialization.Serializable

/**
 * RFC 9457 - “Problem Details for HTTP APIs”
 */
@Serializable
data class Problem(
    val title: String,
    val detail: String,
    val status: Int,
    val errors: List<String>? = null
)
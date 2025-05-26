package org.example.application.usecase

import org.example.domain.UserNotFoundByIdException
import org.example.domain.UserStatus
import org.example.infra.repository.UserRepository
import java.time.Instant
import java.util.*

class RetrieveUser(
    val userRepository: UserRepository
) {
    fun execute(input: String): RetrieveUserOutput {
        val user = userRepository.findById(UUID.fromString(input)) ?: throw UserNotFoundByIdException(input)
        return RetrieveUserOutput(
            userId = user.userId.toString(),
            username = user.username,
            email = user.email,
            status = user.status,
            contracts = user.contracts.map { it.toString() }.toMutableList(),
            lastLoginAt = user.lastLoginAt,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}

data class RetrieveUserOutput(
    val userId: String,
    val username: String,
    val email: String,
    var status: UserStatus,
    val contracts: MutableList<String> = mutableListOf(),
    val lastLoginAt: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant? = null
)
package org.example.domain

import java.time.Instant
import java.util.UUID

data class User(
    val userId: UUID,
    val username: String,
    val password: String,
    val email: String,
    var status: UserStatus,
    val contracts: MutableList<UUID> = mutableListOf(),
    val lastLoginAt: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant? = null
) {
    companion object {
        fun create(
            username: String,
            password: String,
            email: String,
        ): User {
            return User(
                userId = UUID.randomUUID(),
                username = username,
                password = password,
                email = email,
                status = UserStatus.active,
                createdAt = Instant.now()
            )
        }

        fun createWithId(
            userId: UUID,
            username: String,
            password: String,
            email: String,
        ): User {
            return User(
                userId = userId,
                username = username,
                password = password,
                email = email,
                status = UserStatus.active,
                createdAt = Instant.now()
            )
        }
    }

    fun addContract(contractId: UUID) {
        contracts.add(contractId)
    }

    fun activate() {
        status = UserStatus.active
    }

    fun inactivate() {
        status = UserStatus.inactive
    }
}

enum class UserStatus { active, inactive }

class UserNotFoundByIdException(userId: String) : RuntimeException("User not found by ID $userId")
class UserNotFoundException() : RuntimeException("User not found")
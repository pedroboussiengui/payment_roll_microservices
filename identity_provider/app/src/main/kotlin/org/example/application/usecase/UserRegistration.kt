package org.example.application.usecase

import org.example.domain.User
import org.example.infra.hash.PasswordHash
import org.example.infra.repository.UserRepository
import java.util.UUID

class UserRegistration(
    private val userRepository: UserRepository,
    private val passwordHash: PasswordHash
) {
    fun execute(input: UserRegistrationWithIdInput): UserRegistrationOutput {
        val hashPassword = passwordHash.hash(input.password)
        val user = User.createWithId(
            userId = UUID.fromString(input.userId),
            username = input.username,
            password = hashPassword,
            email = input.email
        )
        userRepository.save(user)
        return UserRegistrationOutput(
            userId = user.userId.toString()
        )
    }
}

data class UserRegistrationWithIdInput(
    val userId: String,
    val username: String,
    val password: String,
    val email: String,
)

data class UserRegistrationInput(
    val username: String,
    val password: String,
    val email: String,
)

data class UserRegistrationOutput(
    val userId: String
)
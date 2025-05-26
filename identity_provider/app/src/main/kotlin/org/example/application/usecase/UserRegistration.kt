package org.example.application.usecase

import org.example.domain.User
import org.example.infra.PasswordHash
import org.example.infra.UserRepository

class UserRegistration(
    private val userRepository: UserRepository,
    private val passwordHash: PasswordHash
) {
    fun execute(input: UserRegistrationInput): UserRegistrationOutput {
        val hashPassword = passwordHash.hash(input.password)
        val user = User.create(
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

data class UserRegistrationInput(
    val username: String,
    val password: String,
    val email: String,
)

data class UserRegistrationOutput(
    val userId: String
)
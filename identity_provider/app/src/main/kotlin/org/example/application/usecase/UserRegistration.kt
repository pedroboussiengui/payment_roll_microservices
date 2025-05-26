package org.example.application.usecase

import com.password4j.Password
import org.example.domain.User
import org.example.infra.UserRepository

class UserRegistration(
    val userRepository: UserRepository
) {
    fun execute(input: UserRegistrationInput): UserRegistrationOutput {
        val hashPassword = Password.hash(input.password)
            .addRandomSalt()
            .withArgon2()

        val user = User.create(
            username = input.username,
            password = hashPassword.result,
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
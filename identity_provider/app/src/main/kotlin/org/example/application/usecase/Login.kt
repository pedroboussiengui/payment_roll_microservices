package org.example.application.usecase

import org.example.application.service.JWTService
import org.example.domain.UserNotFoundException
import org.example.infra.PasswordHash
import org.example.infra.UserRepository

class Login(
    private val userRepository: UserRepository,
    private val passwordHash: PasswordHash
) {
    fun execute(login: LoginInput): LoginOutput {
        val user = userRepository.findByUsername(login.username)
                        ?: throw UserNotFoundException()
        val ok = passwordHash.check(login.password, user.password)

        if (!ok || user.username != login.username) {
            throw AuthenticationFailedException("Password or username are incorrect")
        }
        val jwtService = JWTService()
        try {
            val jwt = jwtService.generatePartialToken(user)
            return LoginOutput(
                type = "partial",
                token = jwt
            )
        } catch (e: Exception) {
            throw AuthenticationFailedException(e.message)
        }
    }
}

class AuthenticationFailedException(message: String?) : RuntimeException(message)

data class LoginInput(
    val username: String,
    val password: String
)

data class LoginOutput(
    val type: String,
    val token: String
)
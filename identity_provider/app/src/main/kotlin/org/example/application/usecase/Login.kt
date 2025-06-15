package org.example.application.usecase

import kotlinx.serialization.Serializable
import org.example.application.AuthenticationException
import org.example.application.service.JWTService
import org.example.infra.hash.PasswordHash
import org.example.infra.repository.UserRepository

class Login(
    private val userRepository: UserRepository,
    private val passwordHash: PasswordHash
) {
    fun execute(login: LoginInput): LoginOutput {
        val user = userRepository.findByUsername(login.username)
                ?: throw AuthenticationException.CredentialsFailed()

        val ok = passwordHash.check(login.password, user.password)
        if (!ok || user.username != login.username) {
            throw AuthenticationException.CredentialsFailed()
        }
        val jwtService = JWTService()
        try {
            val jwt = jwtService.generatePartialToken(user)
            return LoginOutput(
                type = "partial",
                token = jwt
            )
        } catch (e: Exception) {
            throw AuthenticationException.InvalidToken(e.message ?: "Failed to generate token")
        }
    }
}

@Serializable
data class LoginInput(
    val username: String,
    val password: String
)

@Serializable
data class LoginOutput(
    val type: String,
    val token: String
)
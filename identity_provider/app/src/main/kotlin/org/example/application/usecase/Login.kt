package org.example.application.usecase

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.password4j.Password
import org.example.application.service.JWTService
import org.example.domain.UserNotFoundException
import org.example.infra.UserRepository
import java.time.Instant



class Login(
    val userRepository: UserRepository
) {
    fun execute(login: LoginInput): LoginOutput {
        val user = userRepository.findByUsername(login.username)
                        ?: throw UserNotFoundException()
        val ok = Password.check(login.password, user.password).withArgon2()

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
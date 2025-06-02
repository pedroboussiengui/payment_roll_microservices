package org.example.application.usecase

import kotlinx.serialization.Serializable
import org.example.application.AuthenticationException
import org.example.application.service.JWTService
import org.example.domain.UserNotFoundByIdException
import org.example.infra.hash.PasswordHash
import org.example.infra.redis.InMemoryDao
import org.example.infra.redis.Session
import org.example.infra.repository.UserRepository
import java.time.Instant
import java.util.UUID

class RefreshToken(
    private val userRepository: UserRepository,
    private val inMemoryDao: InMemoryDao<Session>,
    private val passwordHash: PasswordHash
) {
    fun execute(input: RefreshTokenInput): RefreshTokenOutput {
        // Defino o serviço jwt
        val jwtService = JWTService()
        // refresh token
        val refreshToken = input.refreshToken
        // o refresh token deve conter o id da sessão
        val sessionId: String = jwtService.getSessionId(refreshToken)
            ?: throw Exception("Token do not contains the session identifier")
        // a sessão deve existir e ser válida
        val session = inMemoryDao.get(sessionId)
            ?: throw Exception("Session not found")
        // verificar se o refresh token é válido
        if (!jwtService.isValid(refreshToken)) {
            throw AuthenticationException.InvalidToken("Invalid token")
        }
        // obtém o userId dao subject do refresh token
        val userId = jwtService.getSubjectIfValid(refreshToken)
        // busca no repositório o user pelo id
        val user = userRepository.findById(UUID.fromString(userId))
            ?: throw UserNotFoundByIdException(userId)
        // busca o contract
        val contractId = jwtService.getContractId(refreshToken)
            ?: throw Exception("Token do not contains the contract identifier")
        // gera os novos conjuntos de ‘tokens’
        val instant = Instant.now()
        val newAccessToken = jwtService.generateAccessToken(user, contractId, instant)
        val newRefreshToken = jwtService.generateRefreshToken(user, sessionId, contractId, instant)
        // atualiza o objeto de sessão
        session.refreshTokenHash = passwordHash.hash(newRefreshToken)
        session.refreshedAt = Instant.now().toString()
        // salva em memória sem definir o ttl
        val remainTtl = inMemoryDao.save(sessionId, session)
        // retorna os token gerados
        return RefreshTokenOutput(
            sessionId = sessionId,
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            remainTtl = remainTtl
        )
    }
}

@Serializable
data class RefreshTokenInput(
    val refreshToken: String
)

@Serializable
data class RefreshTokenOutput(
    val sessionId: String,
    val accessToken: String,
    val refreshToken: String,
    val remainTtl: Long
)
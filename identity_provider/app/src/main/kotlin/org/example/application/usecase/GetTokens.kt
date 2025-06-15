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
import java.util.*

class GetTokens(
    val userRepository: UserRepository,
    val inMemoryDao: InMemoryDao<Session>,
    val passwordHash: PasswordHash
) {
    private val jwtService = JWTService()
    fun execute(input: GetTokensInput): GetTokensOutput {

        val partialToken = input.partialToken

        if (!jwtService.isValid(partialToken)) {
            throw AuthenticationException.InvalidToken("Invalid token")
        }

        val userId = jwtService.getSubjectIfValid(partialToken)

        val user = userRepository.findById(UUID.fromString(userId))
            ?: throw UserNotFoundByIdException(userId)

        if (!user.hasContract(UUID.fromString(input.contractId ))) {
            throw Exception("Invalid contract to set")
        }

        val instant = Instant.now()
        val sessionId = UUID.randomUUID().toString()
        val accessToken = jwtService.generateAccessToken(user, input.contractId, instant)
        val refreshToken = jwtService.generateRefreshToken(user, sessionId, input.contractId, instant)

        val session = Session(
            sessionId = sessionId,
            userId = userId,
            refreshTokenHash = passwordHash.hash(refreshToken),
            expiresAt = jwtService.getExpireAt(refreshToken).toString()
        )

        val expireAtInSeconds = jwtService.getExpiresAtAsEpochMillis(refreshToken)
        inMemoryDao.save(session.sessionId, session, expireAtInSeconds)

        return GetTokensOutput(
            sessionId = sessionId,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}

@Serializable
data class GetTokensInput(
    val partialToken: String,
    val contractId: String
)

@Serializable
data class GetTokensOutput(
    val sessionId: String,
    val accessToken: String,
    val refreshToken: String
)
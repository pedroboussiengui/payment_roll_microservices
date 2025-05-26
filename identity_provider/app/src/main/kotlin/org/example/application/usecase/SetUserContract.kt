package org.example.application.usecase

import org.example.application.service.JWTService
import org.example.domain.UserNotFoundByIdException
import org.example.infra.hash.PasswordHash
import org.example.infra.redis.InMemoryDao
import org.example.infra.redis.Session
import org.example.infra.repository.UserRepository
import java.util.*

class SetUserContract(
    val userRepository: UserRepository,
    val inMemoryDao: InMemoryDao<Session>,
    val passwordHash: PasswordHash
) {
    fun execute(input: SetUserContractInput): SetUserContractOutput {
        val jwtService = JWTService()
        val token = input.partialToken
        // todo: check if token is not expired
        val ok = jwtService.verify(token)
        if (!ok) {
            throw Exception("Failed verification token")
        }
        val userId = jwtService.getSubject(token)
        val user = userRepository.findById(UUID.fromString(userId))
            ?: throw UserNotFoundByIdException(userId)
        if (!user.contracts.contains(UUID.fromString(input.contractId ))) {
            Exception("Invalid contract to set")
        }
        val accessToken = jwtService.generateAccessToken(user, input.contractId)

        //todo: create user session, refresh token can revoke user session
        val sessionId = UUID.randomUUID().toString()
        val refreshToken = jwtService.generateRefreshToken(user, sessionId, input.contractId)
        val session = Session(
            sessionId = sessionId,
            userId = userId,
            refreshTokenHash = passwordHash.hash(refreshToken),
            expiresAt = jwtService.getExpireAt(refreshToken).toString()
        )
        val expireAtInSeconds = jwtService.getExpiresAtAsEpochMillis(refreshToken)
        inMemoryDao.save(session.sessionId, session, expireAtInSeconds)
        return SetUserContractOutput(
            sessionId = sessionId,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}

data class SetUserContractInput(
    val partialToken: String,
    val contractId: String
)

data class SetUserContractOutput(
    val sessionId: String,
    val accessToken: String,
    val refreshToken: String
)
package org.example.application.usecase

import org.example.application.service.JWTService
import org.example.domain.UserNotFoundByIdException
import org.example.infra.UserRepository
import java.util.UUID

class SetUserContract(
    val userRepository: UserRepository
) {
    fun execute(input: SetUserContractInput): SetUserContractOutput {
        val jwtService = JWTService()
        val token = input.partialToken
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
        return SetUserContractOutput(
            accessToken = accessToken,
            refreshToken = ""
        )
    }
}

data class SetUserContractInput(
    val partialToken: String,
    val contractId: String
)

data class SetUserContractOutput(
    val accessToken: String,
    val refreshToken: String
)
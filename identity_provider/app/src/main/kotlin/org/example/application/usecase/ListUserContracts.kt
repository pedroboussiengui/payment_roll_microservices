package org.example.application.usecase

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import org.example.application.service.JWTService
import org.example.domain.UserNotFoundByIdException
import org.example.infra.repository.UserRepository
import java.util.UUID

class ListUserContracts(
    private val userRepository: UserRepository
) {
    private val jwtService = JWTService()
    fun execute(authToken: String): List<ContractOutput> {
        jwtService.isValid(authToken)
        val userId = jwtService.getSubjectIfValid(authToken)

        val user = userRepository.findById(UUID.fromString(userId))
            ?: throw UserNotFoundByIdException(userId)

        val output = mutableListOf<ContractOutput>()
        user.contracts.map {
            output.add(
                ContractOutput(
                    id = it.id.toString(),
                    matricula = it.matricula,
                    organization = it.organization,
                    department = it.department
                )
            )
        }
        return output
    }
}

@Serializable
data class ContractOutput(
    val id: String,
    val matricula: String,
    val organization: String,
    val department: String
)